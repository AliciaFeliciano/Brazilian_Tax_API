package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
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

import java.util.List;
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

    //test list tax calculator

    @Test
    public void testGetAllTaxCalculator() {
        TaxCalculator tax1 = new TaxCalculator();
        tax1.setId(1L);
        tax1.setValueTax(100.00);
        TypesTax typesTax1 = new TypesTax();
        typesTax1.setName("ICMS");
        tax1.setTax(typesTax1);

        TaxCalculator tax2 = new TaxCalculator();
        tax2.setId(2L);
        tax2.setValueTax(160.00);
        TypesTax typesTax2 = new TypesTax();
        typesTax2.setName("ISS");
        tax2.setTax(typesTax2);

        List<TaxCalculator> mockTaxCalculatorList = List.of(tax1, tax2);

        Mockito.when(repositoryTaxCalculator.findAll()).thenReturn(mockTaxCalculatorList);

        List<TaxCalculator> allTaxCalculators = serviceTaxCalculator.getAllTaxCalculators();

        assertEquals(2, allTaxCalculators.size());
        assertEquals("ICMS", allTaxCalculators.get(0).getTax().getName());
        assertEquals("ISS", allTaxCalculators.get(1).getTax().getName());
        Mockito.verify(repositoryTaxCalculator, Mockito.times(1)).findAll();
    }

}