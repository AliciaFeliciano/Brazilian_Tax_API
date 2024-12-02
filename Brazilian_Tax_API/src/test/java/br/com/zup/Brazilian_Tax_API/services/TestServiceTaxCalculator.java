package br.com.zup.Brazilian_Tax_API.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TestServiceTaxCalculator {

    @MockitoBean
    private RepositoryTaxCalculator repositoryTaxCalculator;

    @Autowired
    private ServiceTaxCalculator serviceTaxCalculator;

    private TaxCalculator taxCalculator;

    @BeforeEach
    public void setUp() {
        this.taxCalculator = new TaxCalculator();
        taxCalculator.setValueBase("Base Value");
        taxCalculator.setTypesTax("ICMS");
    }

    // Test register TaxCalculator
    @Test
    public void testWhenRegisterTaxCalculatorHappyPath() {
        Mockito.when(repositoryTaxCalculator.existsByValueBaseAndTypesTax(
                Mockito.anyString(), Mockito.anyString()
        )).thenReturn(false);

        TaxCalculator registeredTaxCalculator = serviceTaxCalculator.registerTaxCalculator(taxCalculator);

        assertEquals(taxCalculator.getValueBase(), registeredTaxCalculator.getValueBase());
        assertEquals(taxCalculator.getTypesTax(), registeredTaxCalculator.getTypesTax());

        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).save(Mockito.any(TaxCalculator.class));
    }
}