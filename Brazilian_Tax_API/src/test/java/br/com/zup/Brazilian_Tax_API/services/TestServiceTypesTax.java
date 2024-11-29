package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
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
public class TestServiceTypesTax {

    @MockitoBean
    private RepositoryTypesTax repositoryTypesTax;

    @Autowired
    private ServiceTypesTax serviceTypesTax;

    private TypesTaxRegisterDTO typesTaxRegisterDTO;

    @BeforeEach
    public void setUp() {
        typesTaxRegisterDTO = new TypesTaxRegisterDTO();
        typesTaxRegisterDTO.setName("ICMS");
        typesTaxRegisterDTO.setDescription("Tax on the Circulation of Goods and Services");
        typesTaxRegisterDTO.setAliquota(18.0);
    }

    @Test
    public void testWhenRegisterTypesTaxHappyPath() {
        Mockito.when(repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()
        )).thenReturn(false);

        TypesTax registeredTax = serviceTypesTax.registerTypesTax(typesTaxRegisterDTO);

        assertEquals(typesTaxRegisterDTO.getName(), registeredTax.getName());
        assertEquals(typesTaxRegisterDTO.getDescription(), registeredTax.getDescription());
        assertEquals(typesTaxRegisterDTO.getAliquota(), registeredTax.getAliquota());

        Mockito.verify(repositoryTypesTax, Mockito.times(1)).save(Mockito.any(TypesTax.class));
    }

}



