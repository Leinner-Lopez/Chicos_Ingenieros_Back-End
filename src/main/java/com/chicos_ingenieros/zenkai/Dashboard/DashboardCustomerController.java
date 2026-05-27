package com.chicos_ingenieros.zenkai.Dashboard;

import com.chicos_ingenieros.zenkai.Products.Application.ProductService;
import com.chicos_ingenieros.zenkai.Sales.Application.SaleService;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard/customer")
public class DashboardCustomerController {

    private final ProductService productService;
    private final SaleService saleService;

    @GetMapping("available_products")
    public ResponseEntity<Long> countAvailableProducts() {
        return new ResponseEntity<>(productService.countProducts(), HttpStatus.OK);
    }

    @GetMapping("{userId}/total_purchases")
    public ResponseEntity<Long> countSalesByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(saleService.countSalesByUser(userId), HttpStatus.OK);
    }

    @GetMapping("{userId}/total_spent")
    public ResponseEntity<BigDecimal> getTotalSpentByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(saleService.getTotalSpentByUser(userId), HttpStatus.OK);
    }

    @GetMapping("{userId}/recent_purchases")
    public ResponseEntity<List<SaleHistoryDTO>> getRecentPurchasesByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(saleService.getRecentPurchasesByUser(userId), HttpStatus.OK);
    }
}
