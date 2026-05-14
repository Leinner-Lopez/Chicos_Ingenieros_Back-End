package com.chicos_ingenieros.zenkai.Sales.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetailRepository;
import com.chicos_ingenieros.zenkai.Sales.Application.UseCases.SaleCrudUseCase;
import com.chicos_ingenieros.zenkai.Sales.Application.UseCases.SaleQueryUseCase;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Domain.SaleRepository;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleItemRequest;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaleService implements SaleCrudUseCase, SaleQueryUseCase {
    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final LotRepository lotRepository;
    private final SaleMapper saleMapper;

    @Override
    @Transactional
    public Sale createSale(SaleItemRequest request) {
        int remaining = request.quantity();
        BigDecimal total = BigDecimal.ZERO;
        List<SaleDetail> details = new ArrayList<>();

        // FEFO: lotes ordenados por fecha de vencimiento ASC
        List<Lot> lots = lotRepository
                .findAvailableLotsByProductOrderedByExpiration(request.productId());

        if (lots.isEmpty()) {
            throw new ResourceNotFoundException("Lots not found for this product");
        }

        for (Lot lot : lots) {
            if (remaining <= 0) break;

            int consumed = Math.min(remaining, lot.getStockQuantity());

            // Descontar stock del lote
            lot.setStockQuantity(lot.getStockQuantity() - consumed);
            if (lot.getStockQuantity() == 0) {
                lot.setStatus(LotStatus.EMPTY);
            }
            lotRepository.save(lot);

            SaleDetail detail = SaleDetail.builder()
                    .lotId(lot.getLotId())
                    .quantity(consumed)
                    .unitPrice(lot.getProductPrice())
                    .build();
            details.add(detail);

            total = total.add(lot.getProductPrice()
                    .multiply(BigDecimal.valueOf(consumed)));

            remaining -= consumed;
        }

        if (remaining > 0) {
            throw new IlegalActionException("Insufficient stock for this product");
        }

        // Guardar venta
        Sale sale = Sale.builder()
                .userId(request.userId())
                .saleDate(LocalDateTime.now())
                .totalPrice(total)
                .build();
        Sale savedSale = saleRepository.save(sale);

        // Guardar detalles vinculados a la venta
        List<SaleDetail> savedDetails = details.stream()
                .map(d -> saleDetailRepository.save(
                        SaleDetail.builder()
                                .saleId(savedSale.getSaleId())
                                .lotId(d.getLotId())
                                .quantity(d.getQuantity())
                                .unitPrice(d.getUnitPrice())
                                .build()
                ))
                .toList();

        savedSale.setDetails(savedDetails);
        return savedSale;
    }

    @Override
    public Sale findSaleById(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + saleId));
    }

    @Override
    public List<Sale> findSalesByUser(Long userId) {
        return saleRepository.findByUserId(userId);
    }

    @Override
    public List<SaleResponse> findAllSales() {
        return saleRepository.findAll().stream().map(saleMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void cancelSale(Long saleId) {
        saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + saleId));

        // Revertir stock de cada lote
        saleDetailRepository.findBySaleId(saleId)
                .forEach(detail -> {
                    Lot lot = lotRepository.findById(detail.getLotId());
                    lot.setStockQuantity(lot.getStockQuantity() + detail.getQuantity());
                    lot.setStatus(LotStatus.AVAILABLE);
                    lotRepository.save(lot);
                });

        saleDetailRepository.deleteBySaleId(saleId);
        saleRepository.deleteById(saleId);
    }
}
