package br.com.zup.Brazilian_Tax_API.services.Calculators;

import br.com.zup.Brazilian_Tax_API.interfaces.TaxTypesCalculatorStrategy;

public class ISSCalculator implements TaxTypesCalculatorStrategy {
    private static final double ISS_RATE = 0.05; // 5%

    @Override
    public double calculate(double baseValue) {
        return baseValue * ISS_RATE;
    }
}
