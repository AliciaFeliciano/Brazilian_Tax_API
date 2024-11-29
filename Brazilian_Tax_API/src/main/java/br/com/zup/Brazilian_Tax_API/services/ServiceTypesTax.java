package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        TypesTax typesTaxUpdate = optional.get();

        MapperTypesTax.UpdatesTypesTax(typesTaxUpdate, typesTaxUpdateDTO);

        return repositoryTypesTax.save(typesTaxUpdate);
    }
}
