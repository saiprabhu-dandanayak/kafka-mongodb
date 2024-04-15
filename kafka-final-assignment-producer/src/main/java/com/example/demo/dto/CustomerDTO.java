package com.example.demo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private AddressDTO address;
    private List<ProductDTO> products;

}
