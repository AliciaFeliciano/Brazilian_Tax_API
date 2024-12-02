package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTaxCalculator;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
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
    private RepositoryTypesTax repositoryTypesTax;
    private TaxCalculatorUpdateDTO taxCalculatorUpdateDTO;

    private TypesTax typesTax;

    @BeforeEach
    public void setUp() {
        this.typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");

        this.taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(90.50);
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

        assertEquals(taxCalculator.getValueTax(), registeredTaxCalculator.getValueTax());
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
        assertEquals(taxCalculator.getValueTax(), registeredTaxCalculator.getValueTax());
        assertEquals(taxCalculator.getTax(), registeredTaxCalculator.getTax());
        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).save(Mockito.any(TaxCalculator.class));
    }

    //Test update TaxCalculator

    @Test
    public void testWhenUpdateTaxCalculatorDoesNotHappyPath() {
        Long nonExistentId = 1L;
        Mockito.when(repositoryTaxCalculator.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                serviceTaxCalculator.updateTaxCalculator(nonExistentId, taxCalculatorUpdateDTO)
        );

        Assertions.assertEquals("TaxCalculator not found", exception.getMessage());

        Mockito.verify(repositoryTaxCalculator, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testWhenUpdateTaxCalculatorWithExistingIdShouldCompareAndUpdateSuccessfully() {
        Long existingId = 1L;

        TaxCalculator existingTax = new TaxCalculator();
        existingTax.setId(existingId);
        existingTax.setValueTax(90.50);

        TypesTax typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");
        existingTax.setTax(typesTax);

        TaxCalculatorUpdateDTO updateDTO = new TaxCalculatorUpdateDTO();
        updateDTO.setId(existingId);
        updateDTO.setValueTax(100.00);
        updateDTO.setTaxId(1L);

        Mockito.when(repositoryTaxCalculator.findById(existingId)).thenReturn(Optional.of(existingTax));
        Mockito.when(repositoryTypesTax.findById(1L)).thenReturn(Optional.of(typesTax));
        Mockito.when(repositoryTaxCalculator.save(Mockito.any(TaxCalculator.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaxCalculator updatedTax = serviceTaxCalculator.updateTaxCalculator(existingId, updateDTO);

        assertEquals(100.00, updatedTax.getValueTax());
        assertEquals("ICMS", updatedTax.getTax().getName());

        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).save(existingTax);
    }



}