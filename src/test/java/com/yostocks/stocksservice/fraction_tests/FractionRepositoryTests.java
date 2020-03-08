package com.yostocks.stocksservice.fraction_tests;

import com.yostocks.stocksservice.fraction.Fraction;
import com.yostocks.stocksservice.fraction.IFractionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FractionRepositoryTests {

    @Autowired
    IFractionRepository repo;


    @Test
    public void buyFraction() {
        Fraction fraction = new Fraction(1L, "GOOGL", 366);
        Fraction response = repo.save(fraction);
        assert (!response.equals(null));
    }

}
