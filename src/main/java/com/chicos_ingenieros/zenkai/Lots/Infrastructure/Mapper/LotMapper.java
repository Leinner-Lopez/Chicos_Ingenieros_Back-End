package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface LotMapper {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productPrice", source = "product.price")
    Lot lotEntityToLot(LotEntity lotEntity);

    @Mapping(target = "product.productId", source = "productId")
    LotEntity lotToLotEntity(Lot lot);
}
