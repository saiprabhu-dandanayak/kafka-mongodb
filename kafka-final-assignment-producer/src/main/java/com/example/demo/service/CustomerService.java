package com.example.demo.service;


import com.example.demo.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(String customerId);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(String customerId, CustomerDTO customerDTO);

    void deleteCustomer(String customerId);
}
