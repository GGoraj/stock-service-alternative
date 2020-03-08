package com.yostocks.stocksservice.stock;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCurrentStockPriceRequestModel {

    private String symbol;


    // for deserialisation
    public GetCurrentStockPriceRequestModel(){
        // empty
    }

}
