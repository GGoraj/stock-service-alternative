package com.yostocks.stocksservice.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockDbInitializer {

    private StockService stockService;

    @Autowired
    public StockDbInitializer(StockService stockService){

        this.stockService = stockService;

        Stock google = new Stock("GOOGL", "Lorem ipsum dolor sit amet," +
                " consectetur adipiscing elit. Curabitur egestas, tellus nec consequat faucibus," +
                " nisi massa mollis turpis, vel pulvinar ex ipsum in enim. Maecenas aliquam vel nisl at porttitor.");
        stockService.saveNewStock(google);

        Stock tesla = new Stock("TSLA", "Lorem ipsum dolor sit amet," +
                " consectetur adipiscing elit. Curabitur egestas, tellus nec consequat faucibus," +
                " nisi massa mollis turpis, vel pulvinar ex ipsum in enim. Maecenas aliquam vel nisl at porttitor.");
        stockService.saveNewStock(tesla);

        Stock adobe = new Stock("ADBE", "Lorem ipsum dolor sit amet," +
                " consectetur adipiscing elit. Curabitur egestas, tellus nec consequat faucibus," +
                " nisi massa mollis turpis, vel pulvinar ex ipsum in enim. Maecenas aliquam vel nisl at porttitor.");
        stockService.saveNewStock(adobe);

    }
}
