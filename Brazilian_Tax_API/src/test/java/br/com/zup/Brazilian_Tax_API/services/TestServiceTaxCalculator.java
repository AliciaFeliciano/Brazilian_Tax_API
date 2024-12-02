package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxCalculator;
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
    private TypesTax typesTax;

    @BeforeEach
    public void setUp() {
        this.typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");

        this.taxCalculator = new TaxCalculator();
        taxCalculator.setValueBase(90.50);
        taxCalculator.setTax(typesTax);
    }

    // Test register TaxCalculator
    @Test
    public void testWhenRegisterTaxCalculatorHappyPath() {
        Mockito.when(repositoryTaxCalculator.existsByValueBaseAndTypesTax(
                Mockito.anyDouble(), Mockito.any(TypesTax.class)
        )).thenReturn(false);

        Mockito.when(repositoryTaxCalculator.save(Mockito.any(TaxCalculator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaxCalculator registeredTaxCalculator = serviceTaxCalculator.registerTaxCalculator(taxCalculator);

        assertEquals(taxCalculator.getValueBase(), registeredTaxCalculator.getValueBase());
        assertEquals(taxCalculator.getTax(), registeredTaxCalculator.getTax());

        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).save(Mockito.any(TaxCalculator.class));
    }
}