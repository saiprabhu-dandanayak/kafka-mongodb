package com.example.demo.service;


import com.example.demo.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(String productId);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(String productId, ProductDTO productDTO);

    void deleteProduct(String productId);
}
