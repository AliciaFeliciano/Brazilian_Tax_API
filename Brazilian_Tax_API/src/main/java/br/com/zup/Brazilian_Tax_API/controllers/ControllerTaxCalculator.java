package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorRegisterDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorResponseDTO;
import br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs.TaxCalculatorUpdateDTO;
import br.com.zup.Brazilian_Tax_API.controllers.typesTaxDTOs.TypesTaxResponseDTO;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import br.com.zup.Brazilian_Tax_API.models.TypesTax;
import br.com.zup.Brazilian_Tax_API.services.ServiceTaxCalculator;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTaxCalculator;
import br.com.zup.Brazilian_Tax_API.services.mappers.MapperTypesTax;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax")
public class ControllerTaxCalculator {

    private final ServiceTaxCalculator serviceTaxCalculator;
    private final MapperTaxCalculator mapperTaxCalculator;

    public ControllerTaxCalculator(ServiceTaxCalculator serviceTaxCalculator, MapperTaxCalculator mapperTaxCalculator) {
        this.serviceTaxCalculator = serviceTaxCalculator;
        this.mapperTaxCalculator = mapperTaxCalculator;
    }

    @PostMapping("/calculators")
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

    @GetMapping("/calculators")
    public ResponseEntity<?> getAllTaxCalculator() {
        try {
            List<TaxCalculator> taxCalculatorList = serviceTaxCalculator.getAllTaxCalculators();

            List<TaxCalculatorResponseDTO> responseDTOList = taxCalculatorList.stream()
                    .map(mapperTaxCalculator::fromResponseTaxCalculator)
                    .toList();

            return ResponseEntity.ok(responseDTOList);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(500).body(Map.of("message", exception.getMessage()));
        }
    }

    @PutMapping("/calculators/{id}")
    public ResponseEntity<?> updateTaxCalculator(@PathVariable Long id, @RequestBody TaxCalculatorUpdateDTO taxCalculatorUpdateDTO) {
        try {
            TaxCalculator updatedTaxCalculator = serviceTaxCalculator.updateTaxCalculator(id, taxCalculatorUpdateDTO);
            return ResponseEntity.ok(updatedTaxCalculator);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar o TaxCalculator.");
        }
    }


}
