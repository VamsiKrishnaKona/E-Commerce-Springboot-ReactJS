package com.ecommerce.service;


import com.ecommerce.exceptions.APIException;
import com.ecommerce.exceptions.CategoryAlreadyExistsWithAnotherIdException;
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
    public CategoryDTO addCategory(CategoryDTO categoryRequest)
    {
        Category category = modelMapper.map(categoryRequest, Category.class);

        Category isCategoryExisted = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());

        if(isCategoryExisted != null) throw
                new APIException("Category with name "+categoryRequest.getCategoryName()+" already exists.");

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
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
    public String updateCategory(Long categoryId, CategoryDTO categoryUpdateRequest)
    {
        Category category = modelMapper.map(categoryUpdateRequest, Category.class);
        Optional<Category> optCategory = categoryRepository.findById(categoryId);

        Category alreadyExistedWithDifferentID = categoryRepository.findByCategoryName(category.getCategoryName());

        if(alreadyExistedWithDifferentID != null) throw
                new CategoryAlreadyExistsWithAnotherIdException("Category already exists with another Identity.");

        if(optCategory.isPresent())
        {
            Category existedCategory = optCategory.get();
            existedCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(existedCategory);

            return "category updated successfully";
        }
        else
        {
            throw new ResourceNotFoundException("Category", "CategoryId",  categoryId);
        }
    }
}
