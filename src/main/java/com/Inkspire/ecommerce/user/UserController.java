package com.Inkspire.ecommerce.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Inkspire.ecommerce.dtos.UpdatePasswordDTO;
import com.Inkspire.ecommerce.dtos.UserDTO;
import com.Inkspire.ecommerce.dtos.AtualizaStatusDTO;
import com.Inkspire.ecommerce.dtos.ErrorResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService service;

    @GetMapping
    public List<User> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        try {
            User createdUser = service.create(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = service.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("status/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody AtualizaStatusDTO atualizaStatusDTO) {
        User updatedUser = service.updateAtivo(id, atualizaStatusDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            User updatedUser = service.updatePassword(id, updatePasswordDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
    }
}
