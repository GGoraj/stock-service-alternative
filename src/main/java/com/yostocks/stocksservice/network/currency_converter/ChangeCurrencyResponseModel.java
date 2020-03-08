package com.yostocks.stocksservice.network.currency_converter;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeCurrencyResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("query")
    @Expose
    private Query query;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("result")
    @Expose
    private Double result;

    /**
     * No args constructor for use in serialization
     */
    public ChangeCurrencyResponseModel() {
    }

    /**
     * @param date
     * @param result
     * @param success
     * @param query
     * @param info
     */
    public ChangeCurrencyResponseModel(Boolean success, Query query, Info info, String date, Double result) {
        super();
        this.success = success;
        this.query = query;
        this.info = info;
        this.date = date;
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ChangeCurrencyResponseModel withSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public ChangeCurrencyResponseModel withQuery(Query query) {
        this.query = query;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public ChangeCurrencyResponseModel withInfo(Info info) {
        this.info = info;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ChangeCurrencyResponseModel withDate(String date) {
        this.date = date;
        return this;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public ChangeCurrencyResponseModel withResult(Double result) {
        this.result = result;
        return this;
    }

}




