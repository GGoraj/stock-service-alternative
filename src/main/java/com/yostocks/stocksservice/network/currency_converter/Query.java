package com.yostocks.stocksservice.network.currency_converter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

@SerializedName("from")
@Expose
private String from;
@SerializedName("to")
@Expose
private String to;
@SerializedName("amount")
@Expose
private Integer amount;

/**
* No args constructor for use in serialization
*
*/
public Query() {
}

/**
*
* @param amount
* @param from
* @param to
*/
public Query(String from, String to, Integer amount) {
super();
this.from = from;
this.to = to;
this.amount = amount;
}

public String getFrom() {
return from;
}

public void setFrom(String from) {
this.from = from;
}

public Query withFrom(String from) {
this.from = from;
return this;
}

public String getTo() {
return to;
}

public void setTo(String to) {
this.to = to;
}

public Query withTo(String to) {
this.to = to;
return this;
}

public Integer getAmount() {
return amount;
}

public void setAmount(Integer amount) {
this.amount = amount;
}

public Query withAmount(Integer amount) {
this.amount = amount;
return this;
}

}