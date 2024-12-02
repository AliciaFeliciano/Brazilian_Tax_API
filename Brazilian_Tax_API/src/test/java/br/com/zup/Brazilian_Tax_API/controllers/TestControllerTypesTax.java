package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.services.ServiceTypesTax;
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


@WebMvcTest(ControllerTypesTax.class)
public class TestControllerTypesTax {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ServiceTypesTax serviceTypesTax;

    private ObjectMapper mapper;
    private TypesTax typesTax;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");
        typesTax.setDescription("Tax on the Circulation of Goods and Services");
        typesTax.setAliquota(18.0);
    }

    //Test register
    @Test
    public void testWhenRegisterTypesTaxHappyPath() throws Exception {
        TypesTaxRegisterDTO typesTaxRegisterDTO = new TypesTaxRegisterDTO();
        typesTaxRegisterDTO.setName("ICMS");
        typesTaxRegisterDTO.setDescription("Tax on the Circulation of Goods and Services");
        typesTaxRegisterDTO.setAliquota(18.0);

        String json = mapper.writeValueAsString(typesTaxRegisterDTO);

        Mockito.when(serviceTypesTax.registerTypesTax(Mockito.any(TypesTax.class))).thenReturn(typesTax);
        mvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/tax/types")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("ICMS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Tax on the Circulation of Goods and Services")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aliquota", CoreMatchers.is(18.0)));
    }

    //test de list
    @Test
    public void testWhenGetAllTypesTax() throws Exception {
        TypesTax tax1 = new TypesTax();
        tax1.setId(1L);
        tax1.setName("ICMS");
        tax1.setDescription("Tax on the Circulation of Goods and Services");
        tax1.setAliquota(18.0);

        TypesTax tax2 = new TypesTax();
        tax2.setId(2L);
        tax2.setName("ISS");
        tax2.setDescription("Service Tax");
        tax2.setAliquota(5.0);

        List<TypesTax> mockTaxList = List.of(tax1, tax2);

        Mockito.when(serviceTypesTax.getAllTypesTax()).thenReturn(mockTaxList);

        mvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/tax/types")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is("ICMS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", CoreMatchers.is("Tax on the Circulation of Goods and Services")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].aliquota", CoreMatchers.is(18.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", CoreMatchers.is("ISS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description", CoreMatchers.is("Service Tax")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].aliquota", CoreMatchers.is(5.0)));
    }

    //Test de update
    @Test
    public void testWhenUpdateTypesTaxHappyPath() throws Exception {
        TypesTax typesTax = new TypesTax();
        typesTax.setId(1L);
        typesTax.setName("ICMS");
        typesTax.setDescription("Tax on the Circulation of Goods and Services");
        typesTax.setAliquota(20.0);

        TypesTaxUpdateDTO updateDTO = new TypesTaxUpdateDTO();
        updateDTO.setName("ICMS");
        updateDTO.setDescription("Tax on the Circulation of Goods and Services");
        updateDTO.setAliquota(20.0);

        String json = mapper.writeValueAsString(updateDTO);

        Mockito.when(serviceTypesTax.updateTypesTax(Mockito.anyLong(), Mockito.any(TypesTaxUpdateDTO.class)))
                .thenReturn(typesTax);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/tax/types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("ICMS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Tax on the Circulation of Goods and Services")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aliquota", CoreMatchers.is(20.0)));
    }

    //Test deleted
    @Test
    public void testDeleteTypesTaxWithExistingId() throws Exception {
        Long existingId = 1L;
        Mockito.doNothing().when(serviceTypesTax).deleteTypesTax(existingId);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/tax/types/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(serviceTypesTax, Mockito.times(1)).deleteTypesTax(existingId);
    }


    @Test
    public void testDeleteTypesTaxWithNonExistentId() throws Exception {
        Long nonExistentId = 99L;
        Mockito.doThrow(new RuntimeException("Tax not found"))
                .when(serviceTypesTax).deleteTypesTax(nonExistentId);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/tax/types/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(serviceTypesTax, Mockito.times(1)).deleteTypesTax(nonExistentId);
    }

}
