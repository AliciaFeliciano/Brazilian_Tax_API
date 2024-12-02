package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(typesTax.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("ICMS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Tax on the Circulation of Goods and Services")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aliquota", CoreMatchers.is(18.0)));
    }
}
