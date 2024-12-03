package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypesTax {
    private final RepositoryTypesTax repositoryTypesTax;

    @Autowired
    public ServiceTypesTax(RepositoryTypesTax repositoryTypesTax) {
        this.repositoryTypesTax = repositoryTypesTax;
    }

    public TypesTax registerTypesTax(TypesTax typesTax) {
        boolean exist = repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                typesTax.getName(), typesTax.getDescription(), typesTax.getAliquota()
        );
        if (exist) {
            throw new IllegalArgumentException("The type of tax already exists");
        }
        return repositoryTypesTax.save(typesTax);
    }

    public TypesTax updateTypesTax(Long id, TypesTaxUpdateDTO typesTaxUpdateDTO) {
        TypesTax existingTax = repositoryTypesTax.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found"));

        if (existingTax.getName().equals(typesTaxUpdateDTO.getName()) &&
                existingTax.getDescription().equals(typesTaxUpdateDTO.getDescription()) &&
                existingTax.getAliquota() == typesTaxUpdateDTO.getAliquota()) {
            throw new RuntimeException("No changes detected");
        }

        MapperTypesTax.fromUpdatesTypesTax(existingTax, typesTaxUpdateDTO);
        return repositoryTypesTax.save(existingTax);
    }

    public List<TypesTax> getAllTypesTax() {
        return repositoryTypesTax.findAll();
    }

    public void deleteTypesTax(Long id) {
        if (!repositoryTypesTax.existsById(id)) {
            throw new RuntimeException("Tax not found");
        }
        repositoryTypesTax.deleteById(id);
    }
}
