package com.chicos_ingenieros.zenkai.Categories.Infrastructure;

import com.chicos_ingenieros.zenkai.Categories.Application.CategoryService;
import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category) {
        Category saved = service.saveCategory(category);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> categoryList = service.findAllCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Category category = service.findCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Category> update(@RequestBody Category category) {
        Category categoryDB = service.updateCategory(category.getCategory_id(),category);
        return new ResponseEntity<>(categoryDB, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCategoryById(id);
    }
}
