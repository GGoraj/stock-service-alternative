package com.yostocks.stocksservice.network.currency_converter;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CurrencyExchangeService {

    private final String apiAccessKey = "d87cefa218dd238422923aa6bc1c0b53";
    private final String baseApiPath = "http://data.fixer.io/api/convert?access_key=" + apiAccessKey;

    @Cacheable("convert-currency")
    public double convertCurrency(String from, String to){
        String uri = baseApiPath + "&from=" + from + "&to=" + to + "&amount=" + 1; // only 1 dolar or 1 dkk
        RestTemplate restTemplate = new RestTemplate();
        ChangeCurrencyResponseModel response = restTemplate.getForObject(uri, ChangeCurrencyResponseModel.class);
        double result = response.getResult();
        return result;
    }

}
