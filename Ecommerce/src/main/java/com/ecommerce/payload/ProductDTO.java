package com.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO
{
    private Long productId;

    @NotBlank(message = "Product name must contain a value.")
    @Size(min = 3, message = "Product name must contains least 3 characters.")
    private String productName;
    private String image;

    @NotBlank(message = "product description must contain a value.")
    @Size(min = 6, message = "Product description must contains least 3 characters.")
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}
