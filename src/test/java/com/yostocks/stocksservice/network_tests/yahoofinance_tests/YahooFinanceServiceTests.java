package com.yostocks.stocksservice.network_tests.yahoofinance_tests;

import com.yostocks.stocksservice.stock.StockHistoryModel;
import com.yostocks.stocksservice.network.yahoofinance.YahooFinanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class YahooFinanceServiceTests {

    @Autowired
    YahooFinanceService service;

    String[] stockSymbols = new String[]{
            "GOOGL",
            "TSLA"
    };

    int year;
    String typeOfSearch;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();


    @Test
    void getAllStockHistory_notNull() throws Exception {
        List<List<StockHistoryModel>> stockHistory = service.getFullHistoryOfAllStocks(stockSymbols, year, typeOfSearch);
        assertNotNull(stockHistory);
    }

    @Test
    void getAllStockHistory_invalidStockSymbol() throws Exception {
        stockSymbols[0] = "(*&^";
        /*assertThrows(NullPointerException.class,
                ()->{
                });*/
        List<List<StockHistoryModel>> response = service.getFullHistoryOfAllStocks(stockSymbols, 0, null);
        assertTrue(response.isEmpty());
        stockSymbols[0] = "GOOGL";
    }

    @Test
    void getAllStockHistory_invalidYear() throws Exception {

       /* assertThrows(FileNotFoundException.class,
                ()->{
                });*/

        List<List<StockHistoryModel>> response = service.getFullHistoryOfAllStocks(stockSymbols, 0, null);
        assertTrue(response.isEmpty());

    }

    @Test
    void getAllStockHistory_invalidTypeSearch() throws Exception {
        /*assertThrows(FileNotFoundException.class,
                ()->{
                });*/
        List<List<StockHistoryModel>> response = service.getFullHistoryOfAllStocks(stockSymbols, 0, null);
        assertTrue(response.isEmpty());

    }

    @Test
    void getAllStockHistory_ProperResponse() throws Exception {
        /*assertThrows(FileNotFoundException.class,
                ()->{
                });*/
        List<List<StockHistoryModel>> response = service.getFullHistoryOfAllStocks(stockSymbols, 1, "WEEKLY");

        StockHistoryModel model = response.get(0).get(0);
        assertEquals("GOOGL", model.getSymbol());
    }


}
