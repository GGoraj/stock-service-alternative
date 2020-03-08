package com.yostocks.stocksservice.network.accounting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class AccountingService <T>{

    // preconfigured in StocksServiceApplication class
    private RestTemplate restTemplate;




    @Autowired
    public AccountingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String updateBalanceAndRegisterTransaction(Long user_id, Long fraction_id, TransactionType transactionType, double amount, Timestamp timestamp) {

        HttpHeaders headers = configureHeaders();

        UpdateBalanceAndRegisterTransactionRequestModel requestModel = new UpdateBalanceAndRegisterTransactionRequestModel(user_id, fraction_id, transactionType, amount, timestamp);
        HttpEntity<UpdateBalanceAndRegisterTransactionRequestModel> entity = new HttpEntity<>(requestModel, headers);
        ResponseEntity<UpdateBalanceResponseModel> response = restTemplate.postForEntity("http://accounting-service/balance/update",
                                                                                                entity, UpdateBalanceResponseModel.class);

        if (response.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            return "success";
        } else {
            return "fail";
        }

    }


    public double getBalance(Long user_id) {

        HttpHeaders headers = configureHeaders();

        ResponseEntity<GetBalanceResponseModel> response = restTemplate.getForEntity("http://accounting-service/balance/" + user_id, GetBalanceResponseModel.class);
        String code = String.valueOf(response);
        return Double.valueOf(response.getBody().getResponse());
    }


    public String registerTransaction(Long user_id, Long fraction_id, TransactionType transaction_type, double amount, Timestamp time_stamp) {

        HttpHeaders headers = configureHeaders();

        RegisterTransactionRequestModel requestModel = new RegisterTransactionRequestModel(user_id, fraction_id, transaction_type, amount, time_stamp);
        HttpEntity<RegisterTransactionRequestModel> entity = new HttpEntity<>(requestModel, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://accounting-service/transaction/register", entity, String.class);


        if (response.getStatusCode().equals(HttpStatus.ACCEPTED) && !response.getBody().equals(null)) {
            return "success";
        } else {
            return "fail";
        }

    }

    private HttpHeaders configureHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}