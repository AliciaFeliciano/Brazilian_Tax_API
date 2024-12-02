package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.dtos.TypesTaxUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.services.ServiceTypesTax;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax")
public class ControllerTypesTax {
    @Autowired
    private final ServiceTypesTax serviceTypesTax;

    public ControllerTypesTax(ServiceTypesTax serviceTypesTax) {
        this.serviceTypesTax = serviceTypesTax;
    }

    @PostMapping("/types")
    public ResponseEntity<?> register(@RequestBody @Valid TypesTaxRegisterDTO typesTaxRegisterDTO) {
        try {
            TypesTax typesTax = MapperTypesTax.fromRegisterTypesTax(typesTaxRegisterDTO);
            TypesTax savedTypesTax = serviceTypesTax.registerTypesTax(typesTax);

            TypesTaxResponseDTO responseDTO = MapperTypesTax.fromResponseTypesTax(savedTypesTax);

            return ResponseEntity.status(201).body(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(422).body(Map.of("message", exception.getMessage()));
        }
    }

    @GetMapping("/types")
    public ResponseEntity<?> getAllTypesTax() {
        try {
            List<TypesTax> typesTaxList = serviceTypesTax.getAllTypesTax();

            List<TypesTaxResponseDTO> responseDTOList = typesTaxList.stream()
                    .map(MapperTypesTax::fromResponseTypesTax)
                    .toList();

            return ResponseEntity.ok(responseDTOList);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(500).body(Map.of("message", exception.getMessage()));
        }
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<?> updateTypesTax(
            @PathVariable Long id,
            @RequestBody @Valid TypesTaxUpdateDTO typesTaxUpdateDTO) {
        try {
            TypesTax updatedTypesTax = serviceTypesTax.updateTypesTax(id, typesTaxUpdateDTO);
            TypesTaxResponseDTO responseDTO = MapperTypesTax.fromResponseTypesTax(updatedTypesTax);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(422).body(Map.of("message", exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypesTax(@PathVariable Long id) {
        try {
            serviceTypesTax.deleteTypesTax(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

