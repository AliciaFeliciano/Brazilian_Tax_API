package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorResponseDTO;
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

    @Test
    public void testWhenListTaxCalculatorsHappyPath() throws Exception {
        TaxCalculator taxCalculator1 = new TaxCalculator();
        taxCalculator1.setId(1L);
        taxCalculator1.setValueTax(100.00);
        taxCalculator1.setTax(typesTax);

        TaxCalculator taxCalculator2 = new TaxCalculator();
        taxCalculator2.setId(2L);
        taxCalculator2.setValueTax(200.00);
        taxCalculator2.setTax(typesTax);

        List<TaxCalculator> taxCalculators = List.of(taxCalculator1, taxCalculator2);

        Mockito.when(serviceTaxCalculator.listAllTaxCalculators()).thenReturn(taxCalculators);

        Mockito.when(mapperTaxCalculator.fromResponseTaxCalculator(taxCalculator1))
                .thenReturn(new TaxCalculatorResponseDTO(1L, 100.00, 1L));
        Mockito.when(mapperTaxCalculator.fromResponseTaxCalculator(taxCalculator2))
                .thenReturn(new TaxCalculatorResponseDTO(2L, 200.00, 1L));

        mvc.perform(MockMvcRequestBuilders.get("/api/tax/calculators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].valueTax", CoreMatchers.is(100.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].valueTax", CoreMatchers.is(200.00)));
    }
}

