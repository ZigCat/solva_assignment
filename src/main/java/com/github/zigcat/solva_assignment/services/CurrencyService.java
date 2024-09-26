package com.github.zigcat.solva_assignment.services;

import com.github.zigcat.solva_assignment.entities.CurrencyRate;
import com.github.zigcat.solva_assignment.entities.dto.responses.ExchangeRateResponse;
import com.github.zigcat.solva_assignment.entities.enums.Currency;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import com.github.zigcat.solva_assignment.repositories.CurrencyRateRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class CurrencyService {
    private final RestTemplate restTemplate;
    private final CurrencyRateRepository currencyRateRepository;

    @Value("${alphavantage.key}")
    private String apiKey;

    @Autowired
    public CurrencyService(RestTemplate restTemplate, CurrencyRateRepository currencyRateRepository) {
        this.restTemplate = restTemplate;
        this.currencyRateRepository = currencyRateRepository;
    }

    @PostConstruct
    public void init(){
        log.info("CurrencyService initialized");
        log.info("Checking currency rates...");
        if(currencyRateRepository.findByCurrencyPair(CurrencyPair.KZT_USD).isEmpty()){
            log.warn("Requesting KZT/USD");
            fetchAndSaveCurrencyRates(Currency.USD, Currency.KZT);
        }
        if(currencyRateRepository.findByCurrencyPair(CurrencyPair.RUB_USD).isEmpty()){
            log.warn("Requesting RUB/USD");
            fetchAndSaveCurrencyRates(Currency.USD, Currency.RUB);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void getCurrencyRatesBySchedule(){
        getCurrencyRates();
    }

    private void getCurrencyRates(){
        fetchAndSaveCurrencyRates(Currency.USD, Currency.RUB);
        fetchAndSaveCurrencyRates(Currency.USD, Currency.KZT);
    }

    private void fetchAndSaveCurrencyRates(Currency currencyFrom, Currency currencyTo){
        CurrencyPair currencyPair = checkCurrencyPair(currencyFrom, currencyTo);
        Optional<CurrencyRate> optionalCurrencyRate = currencyRateRepository.findByCurrencyPair(currencyPair);
        CurrencyRate currencyRate;

        if(optionalCurrencyRate.isPresent()){
            currencyRate = optionalCurrencyRate.get();
            currencyRate.setPreviousClose(currencyRate.getClose());
            currencyRate.setDate(LocalDate.now());
        } else {
            currencyRate = new CurrencyRate(currencyPair, null, null);
        }

        String url = String.format("https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s",
                currencyFrom,
                currencyTo,
                apiKey);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        if(response != null){
            currencyRate.setClose(BigDecimal.valueOf(Double.parseDouble(response.getExchangeRate().getExchangeRate())));
            currencyRateRepository.save(currencyRate);
        } else {
            currencyRate.setClose(null);
            currencyRateRepository.save(currencyRate);
        }
    }

    private CurrencyPair checkCurrencyPair(Currency currencyFrom, Currency currencyTo){
        return CurrencyPair.valueOf(currencyTo.toString()+"/"+currencyFrom.toString());
    }
}
