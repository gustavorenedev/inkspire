package com.Inkspire.ecommerce.dtos;

import java.util.Date;
import java.util.List;

import com.Inkspire.ecommerce.address.Address;
import com.Inkspire.ecommerce.telephone.Telephone;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String cpf;
    private String gender;
    private Date birthDate;
    private Telephone phone;
    private List<Address> address;
}
