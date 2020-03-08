package com.yostocks.stocksservice.fraction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SellFractionRequestModel {

    private String stock_symbol;
    private Long user_id;
    private double percent;

}
