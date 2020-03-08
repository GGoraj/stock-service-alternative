package com.yostocks.stocksservice.network.accounting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBalanceAndRegisterTransactionRequestModel {

    @NotNull
    private Long user_id;

    @NotNull
    private Long fraction_id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType transaction_type;

    @NotNull
    private double amount;

    @NotNull
    private Timestamp timestamp;


}