package com.yostocks.stocksservice.fraction_tests;

import com.yostocks.stocksservice.fraction.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FractionServiceTests {

    @Autowired
    FractionService service;
    @Autowired
    IFractionRepository fractionRepo;


    @Test
    void getAllFractions(){
        Collection<Fraction> fractions = service.getAllFractionsByUserId(1L);
        assertNotNull(fractions);
        assertTrue(!fractions.isEmpty());
    }

    @Test
    void createFraction(){

    }
    @Test
    void addFraction(){
        BuyFractionRequestModel request = new BuyFractionRequestModel("GOOGL",1L,120);
        service.buyFraction(request);
    }



    @Test
    void sellFractionByPercentage(){

        // check actual
      //  SellFractionRequestModel sellModel = new SellFractionRequestModel(1,1,new BigDecimal(100));
     //   service.sellFractionByPercentage(sellModel);
    }
}
