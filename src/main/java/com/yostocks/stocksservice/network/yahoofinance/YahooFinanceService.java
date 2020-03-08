package com.yostocks.stocksservice.network.yahoofinance;

import com.yostocks.stocksservice.network.currency_converter.CurrencyExchangeService;
import com.yostocks.stocksservice.stock.StockHistoryModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 *  Following Class provides methods to successfully query Yahoo Finance api
 *  Documentation: https://financequotes-api.com/
 */
@Service
public class YahooFinanceService {

    private CurrencyExchangeService currencyExchangeService;

    public YahooFinanceService(CurrencyExchangeService currencyExchangeService){
        this.currencyExchangeService = currencyExchangeService;
    }

    @Cacheable("full-history-single-stock")
    public  List<StockHistoryModel> getFullHistoryOfSingleStock(String stockName, int year, String searchType){

        List<StockHistoryModel> histories = new ArrayList<>();
        Stock stock = null;
        List<HistoricalQuote> quotes = null;
        if ((searchType == null || searchType.length() <= 1) && (year == 0)) {
            try {
                stock = YahooFinance.get(stockName, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, Integer.valueOf("-" + year));
            try {
                stock = YahooFinance.get(stockName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                quotes = stock.getHistory(from, to, getInterval(searchType));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //quotes = stock.getHistory();
        for (HistoricalQuote quote : quotes) {
            histories.add(new StockHistoryModel(quote.getSymbol(), convertor(quote.getDate()), quote.getHigh(),
                    quote.getLow(), quote.getClose()));

        }


        return histories;
    }


    /**
     *  Get History of All Stocks
     *  This method is called by Rest controller triggered by Android App,
     *  Content is then delivered in order to display
     *  i.a. stock history chart in 'stocks' navigation tab
     */
    @Cacheable("full-history-all-stocks")
    public  List<List<StockHistoryModel>> getFullHistoryOfAllStocks(String[] stockNames, int year, String searchType) {
        List<List<StockHistoryModel>> histories = new ArrayList<>();
        //List<Stock> stocks = null;
        Map<String, Stock> stocks = null;
        List<List<HistoricalQuote>> quotes = new ArrayList<>();
        if ((searchType == null || searchType.length() < 1) && (year == 0)) {
            try {
                stocks = YahooFinance.get(stockNames, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, Integer.valueOf("-" + year));
            try {
                stocks = YahooFinance.get(stockNames, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Stock stock: stocks.values()
                 ) {
                try {
                    quotes.add(stock.getHistory(from, to, getInterval(searchType)) );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (List<HistoricalQuote> list: quotes
            ) {
            List<StockHistoryModel> singleHistory = new ArrayList<>();;
            for (int i =0; i < list.size() -1; i++) {

                singleHistory.add(new StockHistoryModel(list.get(i).getSymbol(), convertor(list.get(i).getDate()),list.get(i).getHigh(),
                        list.get(i).getLow(), list.get(i).getClose()));
            }
            histories.add(singleHistory);
        }

        return histories;
    }


    // not caching - reason: more real experience
    public double getCurrentPriceOfSingleStock(String stockSymbol){
        StockQuote quote = null;
        try {
            YahooFinance.get(stockSymbol).setCurrency("DKK");
            quote = YahooFinance.get(stockSymbol).getQuote();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        double priceUSD = quote.getPrice().doubleValue();
        double currentDollarPriceInDKK = currencyExchangeService.convertCurrency("USD", "DKK");
        double priceDKK = priceUSD*currentDollarPriceInDKK;
        BigDecimal priceRounded = new BigDecimal(priceDKK).setScale(2, RoundingMode.CEILING);
        return priceRounded.doubleValue();
    }

    @Cacheable("calendar-to-string")
    public  String convertor(Calendar cal) {
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    private  Interval getInterval(String searchType) {
        Interval interval = null;
        switch (searchType) {
            case "DAILY":
                interval = Interval.DAILY;
                break;
            case "WEEKLY":
                interval = Interval.WEEKLY;
                break;
            case "MONTHLY":
                interval = Interval.MONTHLY;
                break;

        }
        return interval;
    }





}