package br.com.zup.Brazilian_Tax_API.services.mappers;

import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;

public class MapperTypesTax {
    public static TypesTax RegisterTypesTax(TypesTaxRegisterDTO typesTaxRegisterDTO) {
        TypesTax typesTax = new TypesTax();
        typesTax.setName(typesTaxRegisterDTO.getName());
        typesTax.setDescription(typesTaxRegisterDTO.getDescription());
        typesTax.setAliquota(typesTaxRegisterDTO.getAliquota());
        return typesTax;
    }
}