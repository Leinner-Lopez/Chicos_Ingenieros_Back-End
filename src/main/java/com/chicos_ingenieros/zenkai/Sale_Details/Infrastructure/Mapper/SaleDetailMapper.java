package com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity.SaleDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleDetailMapper {

    @Mapping(source = "sale.saleId", target = "saleId")
    @Mapping(source = "lot.lotId", target = "lotId")
    SaleDetail toDomain(SaleDetailEntity entity);

    @Mapping(source = "saleId", target = "sale.saleId")
    @Mapping(source = "lotId", target = "lot.lotId")
    SaleDetailEntity toEntity(SaleDetail domain);

    List<SaleDetail> toDomainList(List<SaleDetailEntity> entities);
}
