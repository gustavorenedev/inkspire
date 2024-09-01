package com.Inkspire.ecommerce.address;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private String residenceType;
    private String streetType;
    private String street;
    private String number;
    private String neighborhood;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String notes;

}
