package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.repositorys.RepositoryTypesTax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class TestServiceTypesTax {

    @MockitoBean
    private RepositoryTypesTax repositoryTypesTax;

    @Autowired
    private ServiceTypesTax serviceTypesTax;

    private TypesTax typesTax;

    @BeforeEach
    public void setUp() {
        typesTax = new TypesTax();
        typesTax.setId(1);
        typesTax.setName("ICMS");
        typesTax.setDescription("Tax on the Circulation of Goods and Services");
        typesTax.setAliquota(18.0);
    }

    //Teste para verificar se o Registro está correto
    @Test
    public void testWhenRegisterTypesTaxHappyPath() {
        Mockito.when(repositoryTypesTax.existsByNameAndTypesTaxType(Mockito.anyString(), Mockito.any()))
                .thenReturn(false);

        TypesTax returnByService = serviceTypesTax.register(typesTax);

        Mockito.verify(repositoryTypesTax, Mockito.times(1)).save(typesTax);
    }
}