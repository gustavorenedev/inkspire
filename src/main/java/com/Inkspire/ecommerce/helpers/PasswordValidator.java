package com.Inkspire.ecommerce.helpers;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public void validate(String password, String confirmPassword) throws Exception {
        if (password == null || confirmPassword == null) {
            throw new Exception("Senha e confirmação não podem ser nulas.");
        }
        if (!password.equals(confirmPassword)) {
            throw new Exception("As senhas não coincidem.");
        }
        if (!isPasswordStrong(password)) {
            throw new Exception("A senha não atende aos requisitos de força.");
        }
    }

    private boolean isPasswordStrong(String password) {
        return pattern.matcher(password).matches();
    }
}