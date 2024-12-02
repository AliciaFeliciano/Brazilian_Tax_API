package br.com.zup.Brazilian_Tax_API.services.mappers;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;

public class MapperTypesTax {
    public static TypesTax fromRegisterTypesTax(TypesTaxRegisterDTO typesTaxRegisterDTO) {
        TypesTax typesTax = new TypesTax();
        typesTax.setName(typesTaxRegisterDTO.getName());
        typesTax.setDescription(typesTaxRegisterDTO.getDescription());
        typesTax.setAliquota(typesTaxRegisterDTO.getAliquota());
        return typesTax;
    }

    public static void fromUpdatesTypesTax(TypesTax typesTax, TypesTaxUpdateDTO typesTaxUpdateDTO) {
        typesTax.setId(typesTaxUpdateDTO.getId());
        typesTax.setName(typesTaxUpdateDTO.getName());
        typesTax.setDescription(typesTaxUpdateDTO.getDescription());
        typesTax.setAliquota(typesTaxUpdateDTO.getAliquota());
    }

    public static TypesTaxResponseDTO fromResponseTypesTax(TypesTax typesTax) {
        TypesTaxResponseDTO responseDTO = new TypesTaxResponseDTO();
        responseDTO.setId(typesTax.getId());
        responseDTO.setName(typesTax.getName());
        responseDTO.setDescription(typesTax.getDescription());
        responseDTO.setAliquota(typesTax.getAliquota());
        return responseDTO;
    }
}
