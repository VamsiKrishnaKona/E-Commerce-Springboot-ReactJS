package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;

public interface CategoryService 
{
    List<Category> findCategories();

    String addCategory(Category category);

    String deleteCategory(Long categoryId);

    String updateCategory(Long categoryId, Category categoryUpdateRequest);
}
