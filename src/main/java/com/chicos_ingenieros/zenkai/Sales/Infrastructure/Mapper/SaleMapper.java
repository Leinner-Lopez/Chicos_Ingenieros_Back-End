package com.chicos_ingenieros.zenkai.Sales.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Mapper.SaleDetailMapper;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.Entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SaleDetailMapper.class})
public interface SaleMapper {
    @Mapping(source = "user.userId", target = "userId")
    Sale toDomain(SaleEntity entity);

    @Mapping(source = "userId", target = "user.userId")
    SaleEntity toEntity(Sale domain);

    List<Sale> toDomainList(List<SaleEntity> entities);

    SaleResponse toResponse(Sale domain);
    List<SaleResponse> toResponseList(List<Sale> domains);
}
