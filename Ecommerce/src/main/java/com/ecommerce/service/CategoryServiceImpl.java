package com.ecommerce.service;


import com.ecommerce.exceptions.APIException;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.repository.CategoryRepository;
import io.micrometer.core.instrument.config.validate.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService
{
    CategoryRepository categoryRepository;

    ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper)
    {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryResponse findCategories()
    {
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()) throw new
                APIException("categories not found");

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public String addCategory(Category categoryRequest)
    {
        Optional<Category> isCategoryExisted = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());

        if(isCategoryExisted.isPresent()) throw
                new APIException("Category with name "+categoryRequest.getCategoryName()+" already exists.");

        categoryRepository.save(categoryRequest);
        return "category created successfully";
    }

    @Override
    public String deleteCategory(Long categoryId)
    {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isEmpty())
        {
            throw new ResourceNotFoundException("Category", "CategoryId",  categoryId);
        }

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
            throw new ResourceNotFoundException("Category", "CategoryId",  categoryId);
        }
    }
}
