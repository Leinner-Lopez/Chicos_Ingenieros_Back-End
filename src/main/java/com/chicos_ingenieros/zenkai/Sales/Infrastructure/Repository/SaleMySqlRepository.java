package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Domain.SaleRepository;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SaleMySqlRepository implements SaleRepository {

    private final SpringSaleRepository springRepository;
    private final SaleMapper saleMapper;

    @Override
    public Sale save(Sale domain) {
        SaleEntity entity = saleMapper.toEntity(domain);
        return saleMapper.toDomain(springRepository.save(entity));
    }

    @Override
    public Optional<Sale> findById(Long saleId) {
        return springRepository.findById(saleId)
                .map(saleMapper::toDomain);
    }

    @Override
    public List<Sale> findByUserId(Long userId) {
        return saleMapper.toDomainList(
                springRepository.findByUser_UserId(userId)
        );
    }

    @Override
    public void deleteById(Long saleId) {
        springRepository.deleteById(saleId);
    }

    @Override
    public List<Sale> findAll() {
        return springRepository.findAll()
                .stream()
                .map(saleMapper::toDomain)
                .toList();
    }

    @Override
    public List<SaleHistoryDTO> getSaleHistory() {
        return springRepository.getSaleHistory();
    }

    @Override
    public Long countSalesByUser(Long userId) {
        return springRepository.countByUser_UserId(userId);
    }

    @Override
    public BigDecimal getTotalSpentByUser(Long userId) {
        BigDecimal result = springRepository.getTotalSpentByUser(userId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Override
    public List<SaleHistoryDTO> getRecentPurchasesByUser(Long userId) {
        return springRepository.getRecentPurchasesByUser(userId, PageRequest.of(0, 5));
    }
}
