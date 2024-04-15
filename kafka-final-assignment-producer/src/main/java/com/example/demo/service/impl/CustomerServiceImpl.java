package com.example.demo.service.impl;

import com.example.demo.documents.Address;
import com.example.demo.documents.Customer;
import com.example.demo.documents.Product;
import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return convertToDTO(customer);
    }

    private String convertToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("type", "customer");
        customerMap.put("data", customerDTO);
        kafkaTemplate.send("productorder", convertToJson(customerMap));

        customerDTO.getProducts().forEach(productDTO -> {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("type", "product");
            productMap.put("data", productDTO);
            kafkaTemplate.send("productorder", convertToJson(productMap));
        });

        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("type", "updateCustomer");
        updateMap.put("customerId", customerId);
        updateMap.put("data", customerDTO);
    
        kafkaTemplate.send("productorder", convertToJson(updateMap));
    
        return customerDTO; 
    }
    

    @Override
    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
    
        Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("type", "deleteCustomer");
        deleteMap.put("customerId", customerId);
    
        kafkaTemplate.send("productorder", convertToJson(deleteMap));
    }
    

    private AddressDTO convertToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(address, addressDTO);
        return addressDTO;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    private Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        return address;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }

    private Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        Address address = convertToEntity(customerDTO.getAddress());
        customer.setAddress(address);
        List<Product> products = customerDTO.getProducts().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        customer.setProducts(products);

        return customer;

    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        customerDTO.setAddress(convertToDTO(customer.getAddress()));
        List<ProductDTO> productDTOs = customer.getProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        customerDTO.setProducts(productDTOs);

        return customerDTO;
    }

}
