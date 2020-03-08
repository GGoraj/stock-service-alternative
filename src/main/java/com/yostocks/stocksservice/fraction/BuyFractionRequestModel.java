package com.yostocks.stocksservice.fraction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class BuyFractionRequestModel {

    @NotEmpty(message = "stock_symbol empty")
    private String stock_symbol;

    @Min(value = 1, message = "user_id value < 1")
    private Long user_id;

    @Min(value = 100, message = "amount to low - at least 100")
    private double amount;


}
