package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
     public void testWhenRegisterTaxCalculatorDoesNotHappyPath() {
        Mockito.when(repositoryTaxCalculator.existsByValueBaseAndTypesTax(
                Mockito.anyDouble(), Mockito.any(TypesTax.class)
        )).thenReturn(true);

        Mockito.when(repositoryTaxCalculator.save(Mockito.any(TaxCalculator.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaxCalculator registeredTaxCalculator = serviceTaxCalculator.registerTaxCalculator(taxCalculator);
        assertEquals(taxCalculator.getValueBase(), registeredTaxCalculator.getValueBase());
        assertEquals(taxCalculator.getTax(), registeredTaxCalculator.getTax());
        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).save(Mockito.any(TaxCalculator.class));
    }

    //Test update TaxCalculator

    public void testWhenUpdateTaxCalculatorDoesNotHappyPath() {
        Long nonExistentId = 1L;
        TaxCalculator mockTaxCalculator = new TaxCalculator();
        Mockito.when(repositoryTaxCalculator.findById(nonExistentId)).thenReturn(Optional.of(mocktaxCalculator));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                serviceTaxCalculator.updateTypesTax(nonExistentId, taxCalculatorUpdateDTO)
        );

        Assertions.assertEquals("Tax Calculator not found", exception.getMessage());

        Mockito.verify(repositoryTaxCalculator, Mockito.times(0)).save(Mockito.any());
    }

}