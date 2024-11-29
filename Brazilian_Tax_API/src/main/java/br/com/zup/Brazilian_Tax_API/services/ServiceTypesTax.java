package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
