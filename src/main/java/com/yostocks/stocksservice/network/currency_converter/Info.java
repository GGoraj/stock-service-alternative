package com.yostocks.stocksservice.network.currency_converter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

@SerializedName("timestamp")
@Expose
private Integer timestamp;
@SerializedName("rate")
@Expose
private Double rate;

/**
* No args constructor for use in serialization
*
*/
public Info() {
}

/**
*
* @param rate
* @param timestamp
*/
public Info(Integer timestamp, Double rate) {
super();
this.timestamp = timestamp;
this.rate = rate;
}

public Integer getTimestamp() {
return timestamp;
}

public void setTimestamp(Integer timestamp) {
this.timestamp = timestamp;
}

public Info withTimestamp(Integer timestamp) {
this.timestamp = timestamp;
return this;
}

public Double getRate() {
return rate;
}

public void setRate(Double rate) {
this.rate = rate;
}

public Info withRate(Double rate) {
this.rate = rate;
return this;
}

}