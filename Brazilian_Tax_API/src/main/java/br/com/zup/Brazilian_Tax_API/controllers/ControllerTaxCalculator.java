package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.services.ServiceTaxCalculator;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTaxCalculator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax/calculators")
public class ControllerTaxCalculator {

    private final ServiceTaxCalculator serviceTaxCalculator;
    private final MapperTaxCalculator mapperTaxCalculator;

    public ControllerTaxCalculator(ServiceTaxCalculator serviceTaxCalculator, MapperTaxCalculator mapperTaxCalculator) {
        this.serviceTaxCalculator = serviceTaxCalculator;
        this.mapperTaxCalculator = mapperTaxCalculator;
    }

    @PostMapping
    public ResponseEntity<?> registerTaxCalculator(@RequestBody @Valid TaxCalculatorRegisterDTO taxCalculatorRegisterDTO) {
        try {
            TaxCalculator taxCalculator = mapperTaxCalculator.fromRegisterTaxCalculator(taxCalculatorRegisterDTO);
            TaxCalculator savedTaxCalculator = serviceTaxCalculator.registerTaxCalculator(taxCalculator);
            TaxCalculatorResponseDTO responseDTO = mapperTaxCalculator.fromResponseTaxCalculator(savedTaxCalculator);
            return ResponseEntity.status(201).body(responseDTO);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(422).body(Map.of("message", exception.getMessage()));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTaxCalculator() {
        try {
            List<TaxCalculator> taxCalculatorsList = serviceTaxCalculator.getAllTaxCalculators();
            List<TaxCalculatorResponseDTO> responseDTOList = taxCalculatorsList.stream()
                    .map(MapperTaxCalculator::fromResponseTaxCalculator)
                    .toList();
            return ResponseEntity.ok(responseDTOList);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(500).body(Map.of("message", exception.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaxCalculator(
            @PathVariable Long id,
            @RequestBody @Valid TaxCalculatorUpdateDTO taxCalculatorUpdateDTO) {
        try {
            TaxCalculator updatedTaxCalculator = serviceTaxCalculator.updateTaxCalculator(id, taxCalculatorUpdateDTO);
            TaxCalculatorResponseDTO responseDTO = MapperTaxCalculator.fromResponseTaxCalculator(updatedTaxCalculator);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(422).body(Map.of("message", exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxCalculator(@PathVariable Long id) {
        try {
            serviceTaxCalculator.deleteTaxCalculator(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException exception) {
            return ResponseEntity.status(404).build();
        }
    }
}
