package com.chicos_ingenieros.zenkai.Products.Infrastructure;

import com.chicos_ingenieros.zenkai.Products.Application.ProductService;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        Product saved = service.saveProduct(product);
        return new ResponseEntity<>(saved,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> productList = service.findAllProducts();
        return new ResponseEntity<>(productList,HttpStatus.OK);
    }
}
