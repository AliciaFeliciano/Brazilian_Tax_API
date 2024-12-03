package br.com.zup.Brazilian_Tax_API.services.Calculators;

import br.com.zup.Brazilian_Tax_API.interfaces.TaxTypesCalculatorStrategy;

public class ICMSCalculator implements TaxTypesCalculatorStrategy {
    private static final double ICMS_RATE = 0.18; // 18%

    @Override
    public double calculate(double baseValue) {
        return baseValue * ICMS_RATE;
    }
}
