package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTaxCalculator {

    private final RepositoryTaxCalculator repositoryTaxCalculator;

    @Autowired
    public ServiceTaxCalculator(RepositoryTaxCalculator repositoryTaxCalculator) {
        this.repositoryTaxCalculator = repositoryTaxCalculator;
    }

    public TaxCalculator registerTaxCalculator(TaxCalculator taxCalculator) {
        boolean exists = repositoryTaxCalculator.existsByValueBaseAndTypesTax(
                taxCalculator.getValueBase(), taxCalculator.getTax()
        );
        if (exists) {
            throw new IllegalArgumentException("TaxCalculator with the same value base and type already exists.");
        }
        return repositoryTaxCalculator.save(taxCalculator);
    }
}