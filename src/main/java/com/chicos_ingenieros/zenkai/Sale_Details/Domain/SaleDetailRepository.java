package com.chicos_ingenieros.zenkai.Sale_Details.Domain;

import java.util.List;

public interface SaleDetailRepository {
    SaleDetail save(SaleDetail detail);
    List<SaleDetail> findBySaleId(Long saleId);
    void deleteBySaleId(Long saleId);
}
