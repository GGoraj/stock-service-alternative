package com.yostocks.stocksservice.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHistoryOfAllStocksRequestModel {

    private String[] names;
    private int year;
    private String searchType; // ?? MONTHLY or WEEKLY or DAILY
}

