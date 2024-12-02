package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.services.ServiceTaxCalculator;
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

@WebMvcTest(ControllerTaxCalculator.class)
public class TestControllerTaxCalculator {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ServiceTaxCalculator serviceTaxCalculator;

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

    // Test register
    @Test
    public void testWhenRegisterTaxCalculatorHappyPath() throws Exception {
        TaxCalculatorRegisterDTO taxCalculatorRegisterDTO = new TaxCalculatorRegisterDTO();
        taxCalculatorRegisterDTO.setValueTax(100.00);
        taxCalculatorRegisterDTO.setTaxId(1L);

        String json = mapper.writeValueAsString(taxCalculatorRegisterDTO);

        Mockito.when(serviceTaxCalculator.registerTaxCalculator(Mockito.any(TaxCalculator.class))).thenReturn(taxCalculator);

        mvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/tax/calculators")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.valueTax", CoreMatchers.is(90.50)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tax.name", CoreMatchers.is("ICMS")));
    }
}