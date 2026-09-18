package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryRequest)
    {
        CategoryDTO categoryDTO = categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId)
    {
        String response = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,
                                                 @Valid @RequestBody CategoryDTO categoryUpdateRequest)
    {
        String response = categoryService.updateCategory(categoryId, categoryUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
