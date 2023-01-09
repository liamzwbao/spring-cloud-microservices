package edu.neu.microservices.currencyconversionservice.controller;

import edu.neu.microservices.currencyconversionservice.bean.CurrencyConversion;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {
    private final Environment environment;

    public CurrencyConversionController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = Map.of("from", from, "to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class,
                uriVariables);
        CurrencyConversion currencyConversion = responseEntity.getBody();
        if (currencyConversion == null) {
            throw new RuntimeException("Unable to find data from " + from + " to " + to);
        }

        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(environment.getProperty("local.server.port"));
        return currencyConversion;
    }
}
