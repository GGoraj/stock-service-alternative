package com.yostocks.stocksservice.fraction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentModel {

    private Long fraction_id;
    private String stock_symbol;
    private double percent;

}
