package com.yostocks.stocksservice.network.accounting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTransactionRequestModel {


    @NotNull
    private Long user_id;
    @NotNull
    private Long fraction_id;
    @NotNull
    private TransactionType transaction_type;
    @NotNull
    private double amount;
    @NotNull
    private Timestamp time_stamp;
}
