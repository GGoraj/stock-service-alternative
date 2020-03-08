package com.yostocks.stocksservice.stock_tests;

import com.yostocks.stocksservice.stock.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
public class StockControllerTests {

    @Autowired
    StockController stockController;





    @Test
    // /all
    void getAllStocks() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        //Try exchange
        ResponseEntity<List> response = restTemplate.exchange("http://localhost:8091/all", HttpMethod.GET, entity, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(!response.getBody().isEmpty());


    }

    @Test
    // /history/single
    void getAllStockHistory(){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GetHistoryOfAllStocksRequestModel requestModel = new GetHistoryOfAllStocksRequestModel(new String[]{"GOOGL","TSLA"}, 1, "MONTHLY");
        HttpEntity<GetHistoryOfAllStocksRequestModel> entity = new HttpEntity<>(requestModel, headers);


        ResponseEntity<List> response = restTemplate.exchange("http://localhost:8091/history/all", HttpMethod.POST, entity, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());



    }

    @Test
        // /history/all
    void getSingleStockHistory(){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        GetHistoryOfSingleStockRequestModel requestModel = new GetHistoryOfSingleStockRequestModel("GOOGL", 1, "MONTHLY");
        HttpEntity<GetHistoryOfSingleStockRequestModel> entity = new HttpEntity<>(requestModel, headers);

        ResponseEntity<List> response = restTemplate.exchange("http://localhost:8091/history/single", HttpMethod.POST, entity, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


}
