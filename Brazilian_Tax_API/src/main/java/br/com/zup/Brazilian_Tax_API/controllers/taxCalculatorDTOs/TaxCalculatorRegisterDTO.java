package br.com.zup.Brazilian_Tax_API.controllers.taxCalculatorDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxCalculatorRegisterDTO {

    @NotNull
    private double valueTax;

    @NotNull
    private Long taxId;
}
