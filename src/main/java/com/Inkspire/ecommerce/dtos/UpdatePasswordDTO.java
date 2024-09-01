package com.Inkspire.ecommerce.dtos;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String newPassword;
    private String confirmPassword;
}
