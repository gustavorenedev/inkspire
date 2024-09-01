package com.Inkspire.ecommerce.telephone;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "telephones")
@Data
public class Telephone {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ddd;
    private String number;
}
