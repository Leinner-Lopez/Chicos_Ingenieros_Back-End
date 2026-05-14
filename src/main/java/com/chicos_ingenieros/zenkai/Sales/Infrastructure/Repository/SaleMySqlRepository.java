package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Domain.SaleRepository;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
