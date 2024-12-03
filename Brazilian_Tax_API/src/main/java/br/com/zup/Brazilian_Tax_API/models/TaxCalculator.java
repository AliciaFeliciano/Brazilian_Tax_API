package br.com.zup.Brazilian_Tax_API.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TaxCalculator {
    @Id
    @GeneratedValue
    private Long id;

    private double valueTax;

    @ManyToOne
    @JoinColumn(name = "types_tax_id")
    private TypesTax tax;

    @Enumerated(EnumType.STRING)
    private TaxTypesCalculator taxType;
}
