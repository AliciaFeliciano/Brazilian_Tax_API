package br.com.zup.Brazilian_Tax_API.services.mappers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperTaxCalculator {

    private final RepositoryTypesTax repositoryTypesTax;

    @Autowired
    public MapperTaxCalculator(RepositoryTypesTax repositoryTypesTax) {
        this.repositoryTypesTax = repositoryTypesTax;
    }

    public TaxCalculator fromRegisterTaxCalculator(TaxCalculatorRegisterDTO taxCalculatorRegisterDTO) {
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueBase(taxCalculatorRegisterDTO.getValueTax());

        TypesTax typesTax = repositoryTypesTax.findById(taxCalculatorRegisterDTO.getTaxId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax ID: " + taxCalculatorRegisterDTO.getTaxId()));

        taxCalculator.setTax(typesTax);
        return taxCalculator;
    }

    public TaxCalculator fromUpdateTaxCalculator(TaxCalculatorUpdateDTO taxCalculatorUpdateDTO) {
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setId(taxCalculatorUpdateDTO.getId());
        taxCalculator.setValueBase(taxCalculatorUpdateDTO.getValueTax());

        TypesTax typesTax = repositoryTypesTax.findById(taxCalculatorUpdateDTO.getTaxId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax ID: " + taxCalculatorUpdateDTO.getTaxId()));

        taxCalculator.setTax(typesTax);

        return taxCalculator;
    }
}