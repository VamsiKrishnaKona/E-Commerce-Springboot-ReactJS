package com.ecommerce.service;

import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO addProduct(Product product, Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        product.setImage("default.jpg");
        product.setCategory(category);

        double specialPrice = (product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice());

        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts()
    {
        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        return new ProductResponse(productDTOs);
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> productsOfCategory = productRepository.findByCategory(category);

        List<ProductDTO> productsDTOs = productsOfCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        return new ProductResponse(productsDTOs);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword)
    {
        List<Product> productsOfCategory = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        List<ProductDTO> productsDTOs = productsOfCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        return new ProductResponse(productsDTOs);
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product)
    {
        Product existedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        existedProduct.setProductName(product.getProductName());
        existedProduct.setDescription(product.getDescription());
        existedProduct.setQuantity(product.getQuantity());
        existedProduct.setPrice(product.getPrice());
        existedProduct.setDiscount(product.getDiscount());

        Double specialPrice = (product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice());
        existedProduct.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(existedProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId)
    {
        Product existedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.delete(existedProduct);
        return modelMapper.map(existedProduct, ProductDTO.class);
    }


}
