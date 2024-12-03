package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxUpdateDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
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

        when(mapperTaxCalculator.fromRegisterTaxCalculator(Mockito.any(TaxCalculatorRegisterDTO.class)))
                .thenAnswer(invocation -> {
                    TaxCalculator taxCalculator = new TaxCalculator();
                    taxCalculator.setValueTax(100.00);
                    taxCalculator.setTax(typesTax);
                    return taxCalculator;
                });

        when(serviceTaxCalculator.registerTaxCalculator(Mockito.any(TaxCalculator.class)))
                .thenAnswer(invocation -> {
                    taxCalculator.setValueTax(100.00);
                    return taxCalculator;
                });

        when(mapperTaxCalculator.fromResponseTaxCalculator(Mockito.any(TaxCalculator.class)))
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

    //Test listing
    @Test
    public void testWhenGetAllTaxCalculator() throws Exception {
        TypesTax typesTax1 = new TypesTax();
        typesTax1.setId(1L);
        typesTax1.setName("ICMS");
        typesTax1.setDescription("Tax on the Circulation of Goods and Services");

        TaxCalculator tax1 = new TaxCalculator();
        tax1.setId(1L);
        tax1.setValueTax(100.00);
        tax1.setTax(typesTax1);

        TypesTax typesTax2 = new TypesTax();
        typesTax2.setId(2L);
        typesTax2.setName("ISS");
        typesTax2.setDescription("Service Tax");

        TaxCalculator tax2 = new TaxCalculator();
        tax2.setId(2L);
        tax2.setValueTax(160.00);
        tax2.setTax(typesTax2);

        List<TaxCalculator> mockTaxList = List.of(tax1, tax2);

        Mockito.when(serviceTaxCalculator.getAllTaxCalculators()).thenReturn(mockTaxList);

        mvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/tax/calculators")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].valueTax", CoreMatchers.is(100.00)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].valueTax", CoreMatchers.is(160.00)));

    }

    //Test de update
    @Test
    public void testWhenUpdateTaxCalculatorHappyPath() throws Exception {
        TypesTax typesTax1 = new TypesTax();
        typesTax1.setId(1L);
        typesTax1.setName("ICMS");
        typesTax1.setDescription("Tax on the Circulation of Goods and Services");

        TaxCalculatorUpdateDTO taxCalculatorUpdateDTO = new TaxCalculatorUpdateDTO();
        taxCalculatorUpdateDTO.setValueTax(160.00);
        taxCalculatorUpdateDTO.setTaxId(1L);

        TaxCalculator updatedTaxCalculator = new TaxCalculator();
        updatedTaxCalculator.setId(1L);
        updatedTaxCalculator.setValueTax(160.00);
        updatedTaxCalculator.setTax(typesTax1);

        String json = mapper.writeValueAsString(taxCalculatorUpdateDTO);

        Mockito.when(serviceTaxCalculator.updateTaxCalculator(Mockito.eq(1L), Mockito.any(TaxCalculatorUpdateDTO.class)))
                .thenReturn(updatedTaxCalculator);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/tax/calculators/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valueTax", CoreMatchers.is(160.00)))
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

