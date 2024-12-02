package br.com.zup.Brazilian_Tax_API.services.mappers;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;

public class MapperTypesTax {
    public static TypesTax RegisterTypesTax(TypesTaxRegisterDTO typesTaxRegisterDTO) {
        TypesTax typesTax = new TypesTax();
        typesTax.setName(typesTaxRegisterDTO.getName());
        typesTax.setDescription(typesTaxRegisterDTO.getDescription());
        typesTax.setAliquota(typesTaxRegisterDTO.getAliquota());
        return typesTax;
    }

    public static void UpdatesTypesTax(TypesTax typesTax, TypesTaxUpdateDTO typesTaxUpdateDTO) {
        typesTax.setId(typesTaxUpdateDTO.getId());
        typesTax.setName(typesTaxUpdateDTO.getName());
        typesTax.setDescription(typesTaxUpdateDTO.getDescription());
        typesTax.setAliquota(typesTaxUpdateDTO.getAliquota());
    }
}