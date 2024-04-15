package com.example.demo.Mapper;

import com.example.demo.documents.Address;
import com.example.demo.dto.AddressDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO convertToDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(address, addressDTO);
        return addressDTO;
    }

    public Address convertToEntity(AddressDTO addressDTO) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        return address;
    }
}
