package com.chicos_ingenieros.zenkai.Sales.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Products.Application.ProductService;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetailRepository;
import com.chicos_ingenieros.zenkai.Sales.Application.UseCases.SaleCrudUseCase;
import com.chicos_ingenieros.zenkai.Sales.Application.UseCases.SaleHistoryUseCase;
import com.chicos_ingenieros.zenkai.Sales.Application.UseCases.SaleQueryUseCase;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Domain.SaleRepository;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
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
public class SaleService implements SaleCrudUseCase, SaleQueryUseCase, SaleHistoryUseCase {
    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final LotRepository lotRepository;
    private final SaleMapper saleMapper;
    private final ProductService productService;

    @Override
    @Transactional
    public Sale createSale(SaleItemRequest request) {
        ProductDTO productDTO = productService.findProductDTOById(request.productId());
        if (productDTO.totalStock() < request.quantity()) {
            throw new IlegalActionException("Product not enough stock");
        }
        List<Lot> lots = lotRepository.findAvailableLotsByProductOrderedByExpiration(request.productId());
        if (lots.isEmpty()) {
            throw new ResourceNotFoundException("Lots not found for this product");
        }
        List<SaleDetail> details = applyFefo(lots, request.quantity());
        Sale savedSale = buildAndSaveSale(request.userId(), computeTotal(details));
        savedSale.setDetails(persistDetails(savedSale.getSaleId(), details));
        return savedSale;
    }

    private List<SaleDetail> applyFefo(List<Lot> lots, int quantity) {
        int remaining = quantity;
        List<SaleDetail> details = new ArrayList<>();
        for (Lot lot : lots) {
            if (remaining <= 0) break;
            int consumed = Math.min(remaining, lot.getStockQuantity());
            lot.setStockQuantity(lot.getStockQuantity() - consumed);
            if (lot.getStockQuantity() == 0) lot.setStatus(LotStatus.EMPTY);
            lotRepository.save(lot);
            details.add(SaleDetail.builder()
                    .lotId(lot.getLotId())
                    .quantity(consumed)
                    .unitPrice(lot.getProductPrice())
                    .build());
            remaining -= consumed;
        }
        return details;
    }

    private BigDecimal computeTotal(List<SaleDetail> details) {
        return details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Sale buildAndSaveSale(Long userId, BigDecimal total) {
        return saleRepository.save(Sale.builder()
                .userId(userId)
                .saleDate(LocalDateTime.now())
                .totalPrice(total)
                .build());
    }

    private List<SaleDetail> persistDetails(Long saleId, List<SaleDetail> details) {
        return details.stream()
                .map(d -> saleDetailRepository.save(SaleDetail.builder()
                        .saleId(saleId)
                        .lotId(d.getLotId())
                        .quantity(d.getQuantity())
                        .unitPrice(d.getUnitPrice())
                        .build()))
                .toList();
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

    @Override
    public List<SaleHistoryDTO> getSaleHistory() {
        return saleRepository.getSaleHistory();
    }

    @Override
    public Long countSalesByUser(Long userId) {
        return saleRepository.countSalesByUser(userId);
    }

    @Override
    public BigDecimal getTotalSpentByUser(Long userId) {
        return saleRepository.getTotalSpentByUser(userId);
    }

    @Override
    public List<SaleHistoryDTO> getRecentPurchasesByUser(Long userId) {
        return saleRepository.getRecentPurchasesByUser(userId);
    }
}
