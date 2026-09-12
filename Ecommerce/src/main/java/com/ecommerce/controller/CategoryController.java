package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.service.CategoryService;
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
        try {
            List<Category> categories = categoryService.findCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }

    }

    @PostMapping("api/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category categoryRequest)
    {
        try {
            String s = categoryService.addCategory(categoryRequest);
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @DeleteMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId)
    {
        try
        {
            String response = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("api/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId,
                                                 @RequestBody Category categoryUpdateRequest)
    {
        try
        {
            String response = categoryService.updateCategory(categoryId, categoryUpdateRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }

    }
}
