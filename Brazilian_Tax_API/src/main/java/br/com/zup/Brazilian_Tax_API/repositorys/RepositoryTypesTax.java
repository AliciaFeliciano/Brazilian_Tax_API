package br.com.zup.Brazilian_Tax_API.repositorys;

import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTypesTax extends JpaRepository<TypesTax, Long> {
    boolean existsByNameAndDescriptionAndAliquota(String name, String description, Double aliquota);
}