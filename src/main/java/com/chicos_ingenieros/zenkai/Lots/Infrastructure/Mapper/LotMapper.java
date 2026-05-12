package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.Mapper.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface LotMapper {

    Lot lotEntityToLot(LotEntity lotEntity);
    LotEntity lotToLotEntity(Lot lot);
}
