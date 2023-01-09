package edu.neu.microservices.currencyconversionservice.controller;

import edu.neu.microservices.currencyconversionservice.bean.CurrencyConversion;
import edu.neu.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {
    private final Environment environment;
    private final CurrencyExchangeProxy currencyExchangeProxy;

    public CurrencyConversionController(Environment environment, CurrencyExchangeProxy currencyExchangeProxy) {
        this.environment = environment;
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(from, to);
        if (currencyConversion == null) {
            throw new RuntimeException("Unable to find data from " + from + " to " + to);
        }

        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalAmount(quantity.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setEnvironment(environment.getProperty("local.server.port"));
        return currencyConversion;
    }
}
