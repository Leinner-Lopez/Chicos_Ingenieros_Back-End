package com.chicos_ingenieros.zenkai.Sales.Domain;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Sale save(Sale sale);
    Optional<Sale> findById(Long saleId);
    List<Sale> findByUserId(Long userId);
    void deleteById(Long saleId);
    List<Sale> findAll();
}
