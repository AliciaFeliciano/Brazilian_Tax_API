package br.com.zup.Brazilian_Tax_API.services.Calculators;

import br.com.zup.Brazilian_Tax_API.interfaces.TaxTypesCalculatorStrategy;

public class IPICalculator implements TaxTypesCalculatorStrategy {
    private static final double IPI_RATE = 0.10; // 10%

    @Override
    public double calculate(double baseValue) {
        return baseValue * IPI_RATE;
    }
}
