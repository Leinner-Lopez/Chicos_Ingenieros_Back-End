package com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetailRepository;
import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity.SaleDetailEntity;
import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Mapper.SaleDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SaleDetailMySqlRepository implements SaleDetailRepository {

    private final SpringSaleDetailRepository springRepository;
    private final SaleDetailMapper saleDetailMapper;

    @Override
    public SaleDetail save(SaleDetail domain) {
        SaleDetailEntity entity = saleDetailMapper.toEntity(domain);
        return saleDetailMapper.toDomain(springRepository.save(entity));
    }

    @Override
    public List<SaleDetail> findBySaleId(Long saleId) {
        return saleDetailMapper.toDomainList(
                springRepository.findBySale_SaleId(saleId)
        );
    }

    @Override
    public void deleteBySaleId(Long saleId) {
        springRepository.deleteBySale_SaleId(saleId);
    }
}
