package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService 
{
    CategoryResponse findCategories();

    String addCategory(Category category);

    String deleteCategory(Long categoryId);

    String updateCategory(Long categoryId, Category categoryUpdateRequest);
}
