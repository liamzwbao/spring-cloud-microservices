package edu.neu.microservices.currencyconversionservice.proxy;

import edu.neu.microservices.currencyconversionservice.bean.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange", url="localhost:8000/currency-exchange")
public interface CurrencyExchangeProxy {
    @GetMapping("/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to) ;
}
