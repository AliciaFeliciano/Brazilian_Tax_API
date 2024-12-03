package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.services.Calculators.ICMSCalculator;
import br.com.zup.Brazilian_Tax_API.services.Calculators.IPICalculator;
import br.com.zup.Brazilian_Tax_API.services.Calculators.ISSCalculator;
import br.com.zup.Brazilian_Tax_API.interfaces.TaxTypesCalculatorStrategy;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TaxTypesCalculator;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxTypesCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceTaxTypesCalculator {
    private final RepositoryTaxTypesCalculator repositoryTaxTypesCalculator;

    private final Map<TaxTypesCalculator, TaxTypesCalculatorStrategy> taxStrategies = new HashMap<>();

    @Autowired
    public ServiceTaxTypesCalculator(RepositoryTaxTypesCalculator repositoryTaxTypesCalculator) {
        this.repositoryTaxTypesCalculator = repositoryTaxTypesCalculator;
        taxStrategies.put(TaxTypesCalculator.ICMS, new ICMSCalculator());
        taxStrategies.put(TaxTypesCalculator.IPI, new IPICalculator());
        taxStrategies.put(TaxTypesCalculator.ISS, new ISSCalculator());
    }

    public double calculateTax(TaxCalculator taxCalculator) {
        TaxTypesCalculatorStrategy strategy = taxStrategies.get(taxCalculator.getTaxType());
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid tax type: " + taxCalculator.getTaxType());
        }
        return strategy.calculate(taxCalculator.getValueTax());
    }

    public List<TaxTypesCalculator> getAllTaxTypesCalculators() {
        return repositoryTaxTypesCalculator.findAll();
    }

    public Map<TaxTypesCalculator, TaxTypesCalculatorStrategy> getTaxStrategies() {
        return taxStrategies;
    }
}
