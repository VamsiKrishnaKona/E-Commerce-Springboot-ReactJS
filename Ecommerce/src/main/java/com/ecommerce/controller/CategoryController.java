package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CategoryController
{
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService)
    {
        this.categoryService = categoryService;
    }

    @GetMapping("api/public/categories")
    public ResponseEntity<?> getCategories()
    {
        CategoryResponse categories = categoryService.findCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("api/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category categoryRequest)
    {
        String s = categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @DeleteMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId)
    {
        String response = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,
                                                 @RequestBody Category categoryUpdateRequest)
    {
        String response = categoryService.updateCategory(categoryId, categoryUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
