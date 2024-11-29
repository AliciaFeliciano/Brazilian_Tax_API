package br.com.zup.Brazilian_Tax_API.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TypesTax {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private double aliquota;

}
