package com.example.demo.Mapper;

import com.example.demo.documents.Customer;
import com.example.demo.documents.Product;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.ProductDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProductMapper productMapper;

    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        customer.setAddress(addressMapper.convertToEntity(customerDTO.getAddress()));

        List<Product> products = customerDTO.getProducts().stream()
                .map(productMapper::convertToEntity)
                .collect(Collectors.toList());
        customer.setProducts(products);

        return customer;
    }

    public CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        customerDTO.setAddress(addressMapper.convertToDTO(customer.getAddress()));

        List<ProductDTO> productDTOs = customer.getProducts().stream()
                .map(productMapper::convertToDTO)
                .collect(Collectors.toList());
        customerDTO.setProducts(productDTOs);

        return customerDTO;
    }
}
