package br.com.zup.Brazilian_Tax_API.services;
import br.com.zup.Brazilian_Tax_API.models.TaxCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestServiceTaxsCalculators {

    @Test
    void testCalculateTax_ICMS() {
        ServiceTaxCalculator service = new ServiceTaxCalculator();
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxType.ICMS);

        double result = service.calculateTax(taxCalculator);

        assertEquals(180.0, result, 0.01, "ICMS calculation in service is incorrect");
    }

    @Test
    void testCalculateTax_IPI() {
        ServiceTaxCalculator service = new ServiceTaxCalculator();
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxType.IPI);

        double result = service.calculateTax(taxCalculator);

        assertEquals(100.0, result, 0.01, "IPI calculation in service is incorrect");
    }

    @Test
    void testCalculateTax_ISS() {
        ServiceTaxCalculator service = new ServiceTaxCalculator();
        TaxCalculator taxCalculator = new TaxCalculator();
        taxCalculator.setValueTax(1000.0);
        taxCalculator.setTaxType(TaxType.ISS);

        double result = service.calculateTax(taxCalculator);

        assertEquals(50.0, result, 0.01, "ISS calculation in service is incorrect");
    }
}
