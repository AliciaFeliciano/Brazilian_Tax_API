package br.com.zup.Brazilian_Tax_API.repositorys;

import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTaxCalculator extends JpaRepository<TaxCalculator, Long> {
    boolean existsByValueTaxAndTax(double valueTax, TypesTax tax);
}
