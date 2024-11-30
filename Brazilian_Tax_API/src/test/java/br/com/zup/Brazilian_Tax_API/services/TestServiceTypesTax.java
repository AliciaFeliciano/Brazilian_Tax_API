package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestServiceTypesTax {

    @MockitoBean
    private RepositoryTypesTax repositoryTypesTax;

    @Autowired
    private ServiceTypesTax serviceTypesTax;

    private TypesTaxRegisterDTO typesTaxRegisterDTO;

    private TypesTaxUpdateDTO typesTaxUpdateDTO;

    @BeforeEach
    public void setUp() {
        this.typesTaxRegisterDTO = new TypesTaxRegisterDTO();
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

    @Test
    public void testWhenRegisterTypesTaxWithNameAndTypeAlreadyExists(){
        Mockito.when(repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()
        )).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                serviceTypesTax.registerTypesTax(typesTaxRegisterDTO)
        );

        Assertions.assertEquals("The type of tax already exists", exception.getMessage());

        Mockito.verify(repositoryTypesTax, Mockito.times(0)).save(Mockito.any());
    }

    //Teste de atualização
    @Test
    public void testWhenTheTestTypeIsUpdatedWithNonExistentIdShouldFail() {
        Long nonExistentId = 1L;
        TypesTax mockTypesTax = new TypesTax();
        Mockito.when(repositoryTypesTax.findById(nonExistentId)).thenReturn(Optional.of(mockTypesTax));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                serviceTypesTax.updateTypesTax(nonExistentId, typesTaxUpdateDTO)
        );

        Assertions.assertEquals("Tax not found", exception.getMessage());

        Mockito.verify(repositoryTypesTax, Mockito.times(0)).save(Mockito.any());
    }
    @Test
    public void testWhenUpdateTypesTaxWithExistingIdShouldCompareAndUpdateSuccessfully() {
        Long existingId = 1L;

        TypesTax existingTax = new TypesTax();
        existingTax.setId(existingId);
        existingTax.setName("ICMS");
        existingTax.setDescription("Tax on the Circulation of Goods and Services");
        existingTax.setAliquota(18.0);

        TypesTaxUpdateDTO updateDTO = new TypesTaxUpdateDTO();
        updateDTO.setId(existingId);
        updateDTO.setName("ICMS");
        updateDTO.setDescription("Tax on the Circulation of Goods and Services");
        updateDTO.setAliquota(20.0);

        Mockito.when(repositoryTypesTax.findById(existingId)).thenReturn(Optional.of(existingTax));

        Mockito.when(repositoryTypesTax.save(Mockito.any(TypesTax.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TypesTax updatedTax = serviceTypesTax.updateTypesTax(existingId, updateDTO);

        assertEquals("ICMS", updatedTax.getName());
        assertEquals("Tax on the Circulation of Goods and Services", updatedTax.getDescription());
        assertEquals(20.0, updatedTax.getAliquota());

        Mockito.verify(repositoryTypesTax, Mockito.times(1)).save(existingTax);
    }

   @Test
    public void testWhenUpdateTypesTaxWithIsEqualsNonUpdate() {
        Long existingId = 1L;
        Long updatedId = 1L;

        TypesTax existingTax = new TypesTax();

        existingTax.setId(existingId);
        existingTax.setName("ICMS");
        existingTax.setDescription("Tax on the Circulation of Goods and Services");
        existingTax.setAliquota(18.0);
        TypesTaxUpdateDTO updateDTO = new TypesTaxUpdateDTO();
        updateDTO.setId(existingId);
        updateDTO.setName("ICMS");
        updateDTO.setDescription("Tax on the Circulation of Goods and Services");
        updateDTO.setAliquota(18.0);
        Mockito.when(repositoryTypesTax.findById(existingId)).thenReturn(Optional.of(existingTax));

       RuntimeException exception = assertThrows(RuntimeException.class, () ->
               serviceTypesTax.updateTypesTax(updatedId, typesTaxUpdateDTO)
       );

       Assertions.assertEquals("The first rate equals the first", exception.getMessage());

       Mockito.verify(repositoryTypesTax, Mockito.times(0)).save(Mockito.any());
   }

}



