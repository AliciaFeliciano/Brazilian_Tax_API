package br.com.zup.Brazilian_Tax_API.repositorys;

import br.com.zup.Brazilian_Tax_API.models.TaxTypesCalculator;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryTaxTypesCalculator {
    List<TaxTypesCalculator> findAll();
}
