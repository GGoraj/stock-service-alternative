package com.yostocks.stocksservice.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHistoryOfSingleStockRequestModel {

    private String name;
    private int year;
    private String searchType;
}
