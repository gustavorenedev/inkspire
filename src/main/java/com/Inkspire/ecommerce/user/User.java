package com.Inkspire.ecommerce.user;

import java.util.Date;
import java.util.List;

import com.Inkspire.ecommerce.address.Address;
import com.Inkspire.ecommerce.telephone.Telephone;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long Id;
    private String name;
    private String email;
    private String password;
    private String cpf;
    private String gender;
    private boolean ativo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    @OneToOne
    private Telephone phone;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")
    private List<Address> address;

    public boolean getAtivo() {
        return ativo;
    }
}
