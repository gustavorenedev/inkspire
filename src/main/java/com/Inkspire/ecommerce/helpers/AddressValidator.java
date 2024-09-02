package com.Inkspire.ecommerce.helpers;

import java.util.List;

import com.Inkspire.ecommerce.address.Address;
import com.Inkspire.ecommerce.address.AddressType;

public class AddressValidator {

    public void validateAddresses(List<Address> addresses) throws Exception {
        boolean hasBillingAddress = false;
        boolean hasDeliveryAddress = false;

        for (Address address : addresses) {
            // Verifica se todos os campos obrigatórios estão preenchidos e não estão em branco
            if (address.getAddressType() == null ||
                address.getStreet() == null || address.getStreet().trim().isEmpty() ||
                address.getNumber() == null || address.getNumber().trim().isEmpty() ||
                address.getNeighborhood() == null || address.getNeighborhood().trim().isEmpty() ||
                address.getPostalCode() == null || address.getPostalCode().trim().isEmpty() ||
                address.getCity() == null || address.getCity().trim().isEmpty() ||
                address.getState() == null || address.getState().trim().isEmpty() ||
                address.getCountry() == null || address.getCountry().trim().isEmpty()) {

                throw new Exception("Todos os campos de endereço são obrigatórios devem ser preenchidos e não podem estar em branco.");
            }

            // Verifica se existe pelo menos um endereço de cobrança e um de entrega
            if (address.getAddressType() == AddressType.BILLING) {
                hasBillingAddress = true;
            } else if (address.getAddressType() == AddressType.DELIVERY) {
                hasDeliveryAddress = true;
            }
        }

        if (!hasBillingAddress) {
            throw new Exception("É obrigatório registrar ao menos um endereço de cobrança.");
        }

        if (!hasDeliveryAddress) {
            throw new Exception("É obrigatório registrar ao menos um endereço de entrega.");
        }
    }
}
