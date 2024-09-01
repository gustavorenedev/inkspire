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
    

    // private void validateUserFields(User user) {
    //     // RN0012: Verifica se todos os campos obrigatórios estão preenchidos
    //     if (user.getAddress() == null || user.getAddress().isEmpty()) {
    //         throw new IllegalArgumentException("Endereço é obrigatório.");
    //     }

    //     // user.getBirthDate() == null ||
    //     if (user.getGender() == null || user.getName() == null || 
    //         user.getCpf() == null || user.getPhone() == null || user.getEmail() == null || 
    //         user.getPassword() == null || user.getAddress().isEmpty()) {
    //         throw new IllegalArgumentException("Todos os campos obrigatórios para o cadastro do cliente devem ser preenchidos.");
    //     }

    //     // RN0013: CPF e Data de Nascimento não podem ser alterados
    //     if (user.getId() != null) {
    //         Optional<User> existingUser = userRepository.findById(user.getId());
    //         if (existingUser.isPresent()) {
    //             User oldUser = existingUser.get();
    //             if (!oldUser.getCpf().equals(user.getCpf())) {
    //                 throw new IllegalArgumentException("O CPF não pode ser alterado.");
    //             }
    //             if (!oldUser.getBirthDate().equals(user.getBirthDate())) {
    //                 throw new IllegalArgumentException("A data de nascimento não pode ser alterada.");
    //             }
    //         }
    //     }
    // }

    // private void validateAddresses(List<Address> addresses) {
    //     // RN0007 e RN0008: Verifica se ao menos um endereço de cobrança e de entrega foram cadastrados
    //     boolean hasBillingAddress = addresses.stream()
    //     .anyMatch(address -> address.getAddressType() == AddressType.BILLING);
    //     boolean hasDeliveryAddress = addresses.stream()
    //     .anyMatch(address -> address.getAddressType() == AddressType.DELIVERY);

    //     if (!hasBillingAddress) {
    //         throw new IllegalArgumentException("Deve haver ao menos um endereço de cobrança cadastrado.");
    //     }

    //     if (!hasDeliveryAddress) {
    //         throw new IllegalArgumentException("Deve haver ao menos um endereço de entrega cadastrado.");
    //     }

    //     // RN0009: Verifica se todos os campos obrigatórios do endereço estão preenchidos
    //     for (Address address : addresses) {
    //         if (address.getResidenceType() == null || address.getStreetType() == null || 
    //             address.getStreet() == null || address.getNumber() == null || 
    //             address.getNeighborhood() == null || address.getPostalCode() == null || 
    //             address.getCity() == null || address.getState() == null || 
    //             address.getCountry() == null) {
    //             throw new IllegalArgumentException("Todos os campos obrigatórios do endereço devem ser preenchidos.");
    //         }
    //     }
    // }
}
