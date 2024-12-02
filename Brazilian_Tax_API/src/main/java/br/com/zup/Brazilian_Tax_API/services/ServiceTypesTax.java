package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypesTax {

    @Autowired
    private RepositoryTypesTax repositoryTypesTax;

    public TypesTax registerTypesTax(TypesTaxRegisterDTO typesTaxRegisterDTO) {
        TypesTax typesTax = MapperTypesTax.RegisterTypesTax(typesTaxRegisterDTO);
        boolean exist = repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                typesTax.getName(), typesTax.getDescription(), typesTax.getAliquota()
        );
        if (exist) {
            throw new IllegalArgumentException("The type of tax already exists");
        }
        return repositoryTypesTax.save(typesTax);
    }

    public TypesTax updateTypesTax(Long id, TypesTaxUpdateDTO typesTaxUpdateDTO) {
        Optional<TypesTax> optional = repositoryTypesTax.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Tax not found");
        }

        TypesTax existingTax = optional.get();

        if (existingTax.getName().equals(typesTaxUpdateDTO.getName()) &&
                existingTax.getDescription().equals(typesTaxUpdateDTO.getDescription()) &&
                existingTax.getAliquota() == typesTaxUpdateDTO.getAliquota()) {
            throw new RuntimeException("No changes detected");
        }

        MapperTypesTax.UpdatesTypesTax(existingTax, typesTaxUpdateDTO);

        return repositoryTypesTax.save(existingTax);
    }

    public List<TypesTax> getAllTypesTax() {
        final List<TypesTax> allTypesTax = repositoryTypesTax.findAll();
        return allTypesTax;
    }

    public TypesTax deleteTypesTax(Long id) {
        Optional<TypesTax> optional = repositoryTypesTax.findById(id);
        if (optional.isEmpty()) {
           throw new RuntimeException("Tax not found");
        }
       return optional.get();
    }
}
