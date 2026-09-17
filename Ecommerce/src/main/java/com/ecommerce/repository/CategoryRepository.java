package com.ecommerce.repository;

import com.ecommerce.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{

    Category findByCategoryName(@NotBlank(message = "Category cannot be blank.") String categoryName);
}
