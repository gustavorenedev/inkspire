package com.Inkspire.ecommerce.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Inkspire.ecommerce.address.IAddressRepository;
import com.Inkspire.ecommerce.dtos.AtualizaStatusDTO;
import com.Inkspire.ecommerce.dtos.UpdatePasswordDTO;
import com.Inkspire.ecommerce.dtos.UserDTO;
import com.Inkspire.ecommerce.helpers.AddressValidator;
import com.Inkspire.ecommerce.helpers.PasswordEncryptor;
import com.Inkspire.ecommerce.helpers.PasswordValidator;
import com.Inkspire.ecommerce.telephone.ITelephoneRepository;

@Service
public class UserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ITelephoneRepository telephoneRepository;    

    @Autowired
    IAddressRepository addressRepository; 

    private PasswordValidator passwordValidator = new PasswordValidator();
    private PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
    private AddressValidator addressValidator = new AddressValidator();


    public List<User> findAll() {
        return userRepository.findByAtivoTrue();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User create(UserDTO userDTO) throws Exception {
        // Validação da senha e confirmação
        passwordValidator.validate(userDTO.getPassword(), userDTO.getConfirmPassword());

        // Validação dos endereços
        addressValidator.validateAddresses(userDTO.getAddress());

        // Criptografia da senha
        String encryptedPassword = passwordEncryptor.encrypt(userDTO.getPassword());

        // Criação do usuário
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encryptedPassword);
        user.setCpf(userDTO.getCpf());
        user.setGender(userDTO.getGender());
        user.setAtivo(userDTO.isAtivo());
        user.setBirthDate(userDTO.getBirthDate());
        user.setPhone(telephoneRepository.save(userDTO.getPhone()));
        user.setAddress(userDTO.getAddress().stream()
            .map(addressRepository::save)
            .collect(Collectors.toList()));

        // Salvamento do usuário no banco de dados
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        // Verifica se o usuário existe
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Atualiza os detalhes do usuário
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setGender(user.getGender());
        existingUser.setAtivo(user.getAtivo());
        existingUser.setBirthDate(user.getBirthDate());

        // Atualiza os telefones e endereços
        existingUser.setPhone(telephoneRepository.save(user.getPhone()));
        existingUser.setAddress(user.getAddress().stream()
            .map(addressRepository::save)
            .collect(Collectors.toList()));

        // Salva o usuário atualizado
        return userRepository.save(existingUser);
    }

    public User updateAtivo(Long id, AtualizaStatusDTO AtualizaStatusDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        existingUser.setAtivo(AtualizaStatusDTO.isAtivo());

        return userRepository.save(existingUser);
    }
    
    public User updatePassword(Long id, UpdatePasswordDTO updatePasswordDTO) throws Exception {
        // Encontrar o usuário existente
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    
        // Validação da senha e confirmação
        passwordValidator.validate(updatePasswordDTO.getNewPassword(), updatePasswordDTO.getConfirmPassword());
    
        // Criptografia da nova senha
        String encryptedPassword = passwordEncryptor.encrypt(updatePasswordDTO.getNewPassword());
    
        // Atualiza a senha do usuário com a senha criptografada
        existingUser.setPassword(encryptedPassword);
    
        // Salva o usuário atualizado no banco de dados
        return userRepository.save(existingUser);
    }
}
