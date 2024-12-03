package br.com.zup.Brazilian_Tax_API.services.mappers;

import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;

public class MapperTypesTax {

    public static TypesTax fromRegisterTypesTax(TypesTaxRegisterDTO dto) {
        TypesTax typesTax = new TypesTax();
        typesTax.setName(dto.getName());
        typesTax.setDescription(dto.getDescription());
        typesTax.setAliquota(dto.getAliquota());
        return typesTax;
    }

    public static TypesTaxResponseDTO fromResponseTypesTax(TypesTax typesTax) {
        TypesTaxResponseDTO responseDTO = new TypesTaxResponseDTO();
        responseDTO.setId(typesTax.getId());
        responseDTO.setName(typesTax.getName());
        responseDTO.setDescription(typesTax.getDescription());
        responseDTO.setAliquota(typesTax.getAliquota());
        return responseDTO;
    }

    public static void fromUpdatesTypesTax(TypesTax existingTax, TypesTaxUpdateDTO dto) {
        existingTax.setName(dto.getName());
        existingTax.setDescription(dto.getDescription());
        existingTax.setAliquota(dto.getAliquota());
    }
}
