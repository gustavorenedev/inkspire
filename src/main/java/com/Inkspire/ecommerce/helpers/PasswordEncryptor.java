package com.Inkspire.ecommerce.helpers;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {

    // Método para criptografar a senha
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Método para verificar se uma senha corresponde ao hash
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}