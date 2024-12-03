package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.services.Calculators.ICMSCalculator;
import br.com.zup.Brazilian_Tax_API.services.Calculators.IPICalculator;
import br.com.zup.Brazilian_Tax_API.services.Calculators.ISSCalculator;
import br.com.zup.Brazilian_Tax_API.interfaces.TaxTypesCalculatorStrategy;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TaxTypesCalculator;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxTypesCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServiceTaxsCalculators {

    private ServiceTaxTypesCalculator service;
    private RepositoryTaxTypesCalculator repositoryTaxTypesCalculator;

    @BeforeEach
    void setUp() {
        repositoryTaxTypesCalculator = Mockito.mock(RepositoryTaxTypesCalculator.class);

        Map<TaxTypesCalculator, TaxTypesCalculatorStrategy> taxStrategies = new HashMap<>();
        taxStrategies.put(TaxTypesCalculator.ICMS, new ICMSCalculator());
        taxStrategies.put(TaxTypesCalculator.IPI, new IPICalculator());
        taxStrategies.put(TaxTypesCalculator.ISS, new ISSCalculator());

        service = new ServiceTaxTypesCalculator(repositoryTaxTypesCalculator);
        service.getTaxStrategies().putAll(taxStrategies);
    }

    @Test
    void testCalculateTax_ICMS() {
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxTypesCalculator.ICMS);

        double result = service.calculateTax(taxCalculator);

        assertEquals(180.0, result, 0.01, "ICMS calculation in service is incorrect");
    }

    @Test
    void testCalculateTax_IPI() {
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxTypesCalculator.IPI);

        double result = service.calculateTax(taxCalculator);

        assertEquals(100.0, result, 0.01, "IPI calculation in service is incorrect");
    }

    @Test
    void testCalculateTax_ISS() {
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxTypesCalculator.ISS);

        double result = service.calculateTax(taxCalculator);

        assertEquals(50.0, result, 0.01, "ISS calculation in service is incorrect");
    }
}