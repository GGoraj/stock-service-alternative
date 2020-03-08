package com.yostocks.stocksservice.network.accounting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBalanceResponseModel {

    @SerializedName("response")
    @Expose
    private String response;



}
