package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService 
{
    CategoryResponse findCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO category);

    String deleteCategory(Long categoryId);

    String updateCategory(Long categoryId, CategoryDTO categoryUpdateRequest);
}
