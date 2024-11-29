package br.com.zup.Brazilian_Tax_API.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypesTax {

    private long id;
    private String name;
    private String description;
    private double aliquota;

}
