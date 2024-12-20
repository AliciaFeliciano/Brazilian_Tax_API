package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxCalculator;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTaxCalculator {
    private final RepositoryTaxCalculator repositoryTaxCalculator;
    private final RepositoryTypesTax repositoryTypesTax;

    @Autowired
    public ServiceTaxCalculator(RepositoryTaxCalculator repositoryTaxCalculator, RepositoryTypesTax repositoryTypesTax) {
        this.repositoryTaxCalculator = repositoryTaxCalculator;
        this.repositoryTypesTax = repositoryTypesTax;
    }

    public TaxCalculator registerTaxCalculator(TaxCalculator taxCalculator) {
        boolean exists = repositoryTaxCalculator.existsByValueTaxAndTax(
                taxCalculator.getValueTax(), taxCalculator.getTax()
        );
        if (exists) {
            throw new IllegalArgumentException("TaxCalculator with the same value and type already exists.");
        }
        return repositoryTaxCalculator.save(taxCalculator);
    }

    public TaxCalculator updateTaxCalculator(Long id, TaxCalculatorUpdateDTO taxCalculatorUpdateDTO) {
        TaxCalculator existingTaxCalculator = repositoryTaxCalculator.findById(id)
                .orElseThrow(() -> new RuntimeException("TaxCalculator not found"));

        existingTaxCalculator.setValueTax(taxCalculatorUpdateDTO.getValueTax());
        TypesTax typesTax = repositoryTypesTax.findById(taxCalculatorUpdateDTO.getTaxId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax ID: " + taxCalculatorUpdateDTO.getTaxId()));
        existingTaxCalculator.setTax(typesTax);

        return repositoryTaxCalculator.save(existingTaxCalculator);
    }

    public List<TaxCalculator> getAllTaxCalculators() {
        return repositoryTaxCalculator.findAll();
    }

    public void deleteTaxCalculator(Long id) {
        if (!repositoryTaxCalculator.existsById(id)) {
            throw new RuntimeException("TaxCalculator not found");
        }
        repositoryTaxCalculator.deleteById(id);
    }
}