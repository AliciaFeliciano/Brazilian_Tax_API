package br.com.zup.Brazilian_Tax_API.controllers;

import br.com.zup.Brazilian_Tax_API.models.TaxTypesCalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tax/types-calculator")
public class ControllerTaxTypesCalculator {

    @GetMapping
    public ResponseEntity<List<TaxTypesCalculator>> getAllTaxTypesCalculator() {
        List<TaxTypesCalculator> taxTypes = Arrays.asList(TaxTypesCalculator.values());
        return ResponseEntity.ok(taxTypes);
    }
}