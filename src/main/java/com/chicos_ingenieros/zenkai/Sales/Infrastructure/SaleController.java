package com.chicos_ingenieros.zenkai.Sales.Infrastructure;

import com.chicos_ingenieros.zenkai.Sales.Application.SaleService;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleItemRequest;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleItemRequest request) {
        Sale sale = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> findAllSales() {
        return ResponseEntity.ok(saleService.findAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.findSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Sale>> getSalesByUser(@PathVariable Long id) {
        List<Sale> sales = saleService.findSalesByUser(id);
        return ResponseEntity.ok(sales);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSale(@PathVariable Long id) {
        saleService.cancelSale(id);
        return ResponseEntity.noContent().build();
    }
}
