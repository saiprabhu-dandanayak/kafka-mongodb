package com.example.demo.service.impl;

import com.example.demo.documents.Product;
import com.example.demo.dto.ProductDTO;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        product = productRepository.save(product);
        kafkaTemplate.send("productorder", convertToMap("createProduct", product.getId(), productDTO));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        BeanUtils.copyProperties(productDTO, existingProduct);
        existingProduct.setId(productId);
        existingProduct = productRepository.save(existingProduct);
        kafkaTemplate.send("productorder", convertToMap("updateProduct", productId, productDTO));

        return convertToDTO(existingProduct);
    }

    @Override
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);        
        kafkaTemplate.send("productorder", convertToMap("deleteProduct", productId, null));
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }

    private Map<String, Object> convertToMap(String action, String productId, ProductDTO productDTO) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("action", action);
        dataMap.put("productId", productId);
        if (productDTO != null) {
            dataMap.put("product", productDTO);
        }
        return dataMap;
    }
}
