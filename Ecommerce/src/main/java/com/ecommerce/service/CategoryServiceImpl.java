package com.ecommerce.service;


import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService
{
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> findCategories()
    {
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()) throw new
                ResponseStatusException(HttpStatus.NOT_FOUND, "categories not found");

        return categories;
    }

    @Override
    public String addCategory(Category categoryRequest)
    {
        categoryRepository.save(categoryRequest);
        return "category created successfully";
    }

    @Override
    public String deleteCategory(Long categoryId)
    {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isEmpty())
            throw new
                ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found");

        categoryRepository.deleteById(categoryId);
        return "Category with ID " + categoryId + "deleted successfully.";
    }

    @Override
    public String updateCategory(Long categoryId, Category categoryUpdateRequest)
    {
        Optional<Category> optCategory = categoryRepository.findById(categoryId);

        if(optCategory.isPresent())
        {
            Category existedCategory = optCategory.get();

            existedCategory.setCategoryName(categoryUpdateRequest.getCategoryName());

            categoryRepository.save(existedCategory);

            return "category updated successfully";
        }
        else
        {
            throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Category with ID " + categoryId + " not found");
        }
    }
}
