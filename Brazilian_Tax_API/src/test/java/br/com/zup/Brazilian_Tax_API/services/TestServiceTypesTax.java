package br.com.zup.Brazilian_Tax_API.services;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxUpdateDTO;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TestServiceTypesTax {

    @MockitoBean
    private RepositoryTypesTax repositoryTypesTax;

    @Autowired
    private ServiceTypesTax serviceTypesTax;

    private TypesTax typesTax;

    private TypesTaxUpdateDTO typesTaxUpdateDTO;

    @BeforeEach
    public void setUp() {
        this.typesTax = new TypesTax();
        typesTax.setName("ICMS");
        typesTax.setDescription("Tax on the Circulation of Goods and Services");
        typesTax.setAliquota(18.0);
    }

    @Test
    public void testWhenRegisterTypesTaxHappyPath() {
        Mockito.when(repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()
        )).thenReturn(false);

        TypesTax registeredTax = serviceTypesTax.registerTypesTax(typesTax);

        assertEquals(typesTax.getName(), registeredTax.getName());
        assertEquals(typesTax.getDescription(), registeredTax.getDescription());
        assertEquals(typesTax.getAliquota(), registeredTax.getAliquota());

        Mockito.verify(repositoryTypesTax, Mockito.times(1)).save(Mockito.any(TypesTax.class));
    }

    @Test
    public void testWhenRegisterTypesTaxWithNameAndTypeAlreadyExists(){
        Mockito.when(repositoryTypesTax.existsByNameAndDescriptionAndAliquota(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble()
        )).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                serviceTypesTax.registerTypesTax(typesTax)
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
   @Test
    public void testWhenUpdateTypesTaxLackOfInformation() {
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
        updateDTO.setAliquota(20.0);
        Mockito.when(repositoryTypesTax.findById(existingId)).thenReturn(Optional.of(existingTax));

       RuntimeException exception = assertThrows(RuntimeException.class, () ->
               serviceTypesTax.updateTypesTax(updatedId, typesTaxUpdateDTO)
       );

       Assertions.assertEquals("Tax lacks information ", exception.getMessage());

       Mockito.verify(repositoryTypesTax, Mockito.times(0)).save(Mockito.any());
   }

   //Teste para listagem

    @Test
    public void testGetAllTypesTax() {
        TypesTax tax1 = new TypesTax();
        tax1.setId(1L);
        tax1.setName("ICMS");
        tax1.setDescription("Tax on the Circulation of Goods and Services");
        tax1.setAliquota(18.0);

        TypesTax tax2 = new TypesTax();
        tax2.setId(2L);
        tax2.setName("ISS");
        tax2.setDescription("Imposto sobre Serviços");
        tax2.setAliquota(5.0);

        List<TypesTax> mockTaxList = List.of(tax1, tax2);

        Mockito.when(repositoryTypesTax.findAll()).thenReturn(mockTaxList);

        List<TypesTax> allTypesTax = serviceTypesTax.getAllTypesTax();

        assertEquals(2, allTypesTax.size());
        assertEquals("ICMS", allTypesTax.get(0).getName());
        assertEquals("ISS", allTypesTax.get(1).getName());
        Mockito.verify(repositoryTypesTax, Mockito.times(1)).findAll();
    }

    //Test para deletar uma taxa

    @Test
    public void testDeleteTypesTaxWithExistingId() {
        Long existingId = 1L;
        TypesTax existingTax = new TypesTax();
        existingTax.setId(existingId);
        existingTax.setName("ICMS");
        existingTax.setDescription("Tax on the Circulation of Goods and Services");
        existingTax.setAliquota(18.0);

        Mockito.when(repositoryTypesTax.findById(existingId)).thenReturn(Optional.of(existingTax));

        serviceTypesTax.deleteTypesTax(existingId);

        Mockito.verify(repositoryTypesTax, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void testDeleteTypesTaxWithNonExistentId() {
        Long nonExistentId = 1L;

        Mockito.when(repositoryTypesTax.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> serviceTypesTax.deleteTypesTax(nonExistentId));

        Assertions.assertEquals("Tax not found", exception.getMessage());
        Mockito.verify(repositoryTypesTax, Mockito.times(0)).deleteById(nonExistentId);
    }

}



