package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.services.ServiceTaxCalculator;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTaxCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ControllerTaxCalculator.class)
public class TestControllerTaxCalculator {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ServiceTaxCalculator serviceTaxCalculator;

    @MockitoBean
    private MapperTaxCalculator mapperTaxCalculator;

    private ObjectMapper mapper;
    private TaxCalculator taxCalculator;
    private TypesTax typesTax;

    @BeforeEach
    public void setUp() {
        this.mapper = new ObjectMapper();
        this.typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");

        this.taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(90.50);
        taxCalculator.setTax(typesTax);
    }

    @Test
    public void testWhenRegisterTaxCalculatorHappyPath() throws Exception {
        TaxCalculatorRegisterDTO taxCalculatorRegisterDTO = new TaxCalculatorRegisterDTO();
        taxCalculatorRegisterDTO.setValueTax(100.00);
        taxCalculatorRegisterDTO.setTaxId(1L);

        String json = mapper.writeValueAsString(taxCalculatorRegisterDTO);

        Mockito.when(mapperTaxCalculator.fromRegisterTaxCalculator(Mockito.any(TaxCalculatorRegisterDTO.class)))
                .thenAnswer(invocation -> {
                    TaxCalculator taxCalculator = new TaxCalculator();
                    taxCalculator.setValueTax(100.00);
                    taxCalculator.setTax(typesTax);
                    return taxCalculator;
                });

        Mockito.when(serviceTaxCalculator.registerTaxCalculator(Mockito.any(TaxCalculator.class)))
                .thenAnswer(invocation -> {
                    taxCalculator.setValueTax(100.00);
                    return taxCalculator;
                });

        Mockito.when(mapperTaxCalculator.fromResponseTaxCalculator(Mockito.any(TaxCalculator.class)))
                .thenAnswer(invocation -> {
                    TaxCalculatorResponseDTO responseDTO = new TaxCalculatorResponseDTO();
                    responseDTO.setId(1L);
                    responseDTO.setValueTax(100.00);
                    responseDTO.setTaxId(1L);
                    return responseDTO;
                });

        mvc.perform(MockMvcRequestBuilders.post("/api/tax/calculators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valueTax", CoreMatchers.is(100.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxId", CoreMatchers.is(1)));
    }
    //Test deleted
    @Test
    public void testDeleteTaxCalculatorWithExistingId() throws Exception {
        Long existingId = 1L;
        Mockito.doNothing().when(serviceTaxCalculator).deleteTaxCalculator(existingId);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/tax/calculators/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(serviceTaxCalculator, Mockito.times(1)).deleteTaxCalculator(existingId);
    }

    @Test
    public void testDeleteTypesTaxWithNonExistentId() throws Exception {
        Long nonExistentId = 99L;
        Mockito.doThrow(new RuntimeException("Tax not found"))
                .when(serviceTaxCalculator).deleteTaxCalculator(nonExistentId);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/tax/calculators/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(serviceTaxCalculator, Mockito.times(1)).deleteTaxCalculator(nonExistentId);
    }


}

