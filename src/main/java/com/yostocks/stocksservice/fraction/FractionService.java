package com.yostocks.stocksservice.fraction;

import com.yostocks.stocksservice.network.accounting.AccountingService;
import com.yostocks.stocksservice.network.accounting.TransactionType;
import com.yostocks.stocksservice.network.yahoofinance.YahooFinanceService;
import com.yostocks.stocksservice.stock.IStockRepository;
import com.yostocks.stocksservice.stock.Stock;
import com.yostocks.stocksservice.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Service
public class FractionService {

    private IFractionRepository repoFractions;

    private YahooFinanceService yahooFinanceService;
    private AccountingService accountingService;
    private StockService stockService;


    @Autowired
    public FractionService(IFractionRepository repoFractions,
                           YahooFinanceService yahooFinanceService,
                           AccountingService accountingService,
                           StockService stockService) {

        this.repoFractions = repoFractions;
        this.yahooFinanceService = yahooFinanceService;
        this.accountingService = accountingService;
        this.stockService = stockService;
    }


    @Transactional
    public double sellFractionByPercentage(SellFractionRequestModel requestModel) {

        // check if percent is valid
        if (requestModel.getPercent() <= 0) {
            return 0;
        }



        /**
         *  NOTE: Each Fraction has to be sold to its parent Stock
         *  this way ensuring order within database: stock percentage is never over 100 in any of the stocks
         */

        // load up the fraction
        Optional<Fraction> f = Optional.ofNullable(repoFractions.findFractionByStockSymbolAndUserId(requestModel.getUser_id(), requestModel.getStock_symbol()));
        Fraction fraction = null;
        if(!f.isPresent()){
            return -1;
        }
        fraction = f.get();

        // check if request % is <= fraction % (its what user already has)
        double fractionPercent = fraction.getPercent();
        double requestedPercent = requestModel.getPercent();

        // return fraction's current percent if % is insufficient
        if (fractionPercent < requestedPercent) {
            return -2;
        }


        // get current stock price
        double currentStockPrice = yahooFinanceService.getCurrentPriceOfSingleStock(requestModel.getStock_symbol());
        // convert to BigDecimal for more accurate calculations
        BigDecimal stockPrice = BigDecimal.valueOf(currentStockPrice);


        //double currentBalance = accountingService.getBalance(requestModel.getUser_id());
        // calculate value of requested percentage: gain = stockPrice* 100/%
        BigDecimal percentToCashValue = stockPrice.multiply(new BigDecimal(requestedPercent));
        percentToCashValue = percentToCashValue.divide(BigDecimal.valueOf(100));
        percentToCashValue = percentToCashValue.setScale(2, RoundingMode.UP);

        // subtract request % from existing fraction %
        double newFractionPercent = (BigDecimal.valueOf(fractionPercent).subtract(BigDecimal.valueOf(requestedPercent))).doubleValue();
        if(newFractionPercent == 0){
            repoFractions.deleteById(fraction.getId());
        }
        else{
            fraction.setPercent(newFractionPercent);
            Fraction fractionDb = repoFractions.save(fraction);

            // if fraction was not saved in database - > return '3'
            if(fractionDb == null || fractionDb.equals(null)){
                return -3; // not sold
            }
        }

        // update balance
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String result = accountingService.updateBalanceAndRegisterTransaction(requestModel.getUser_id(), fraction.getId(),
                TransactionType.SALE, percentToCashValue.doubleValue(),
                timestamp);

        //"stock fractions sold";
        return 1;
    }


    // returns key value pair <stock_id, percent_invested> for requested user_id
    public Collection<Fraction> getAllFractionsByUserId(Long user_id) {

        Collection<Fraction> userFractions;
        userFractions = repoFractions.findAllByUserId(user_id);
        return userFractions;

    }

    public HashMap<Long, String> getStockSymbolsByAllFractions(Collection<Fraction> fractions) {

        HashMap<Long, String> fractionToStockSymbolMap = new HashMap<>();

        for (Fraction f : fractions
        ) {

            String stockSymbol = repoFractions.findStockSymbolById(f.getId());
            fractionToStockSymbolMap.put(f.getId(), stockSymbol);
        }

        return fractionToStockSymbolMap;
    }


    /**
     * This method assigns percentage ownership(of User) - fraction to stock.
     * - calculate 1% provision and subtracts it from user's requested amount
     * - check current stock market price
     * in each step we try to update stock as fast as possible
     * in order to make whole procedure in shortest time possible to avoid the resource lock (Positive Lock)
     *
     * @return
     */


    public String buyFraction(BuyFractionRequestModel buyFractionRequestModel) {

        //check if user's balance is > then request amount
        double balance = accountingService.getBalance(buyFractionRequestModel.getUser_id());
        if(balance < buyFractionRequestModel.getAmount()){
            return "insufficient credit";
        }


        // check the actual stock price with YahooFinance Api
        String stock_symbol = buyFractionRequestModel.getStock_symbol();
        Double double_marketPrice = yahooFinanceService.getCurrentPriceOfSingleStock(stock_symbol);

        // in case the yahoo finance service was not returning anything
        if(double_marketPrice == null){
            return null;
        }
        // wrap market_price with BigDecimal for precise calculations
        BigDecimal market_priceBD = new BigDecimal(double_marketPrice).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal one_hundred_percentBD = new BigDecimal(100);

        // calculate user's fraction percentage
        BigDecimal amountBD = BigDecimal.valueOf(buyFractionRequestModel.getAmount());
        BigDecimal requestPercent = amountBD.multiply(one_hundred_percentBD).divide(market_priceBD, 3, RoundingMode.DOWN);

        return createFraction(amountBD, requestPercent, buyFractionRequestModel.getUser_id(), buyFractionRequestModel.getStock_symbol());
    }


    public String createFraction(BigDecimal amountAfterProvision, BigDecimal requestPercent, Long user_id, String stock_symbol) throws ObjectOptimisticLockingFailureException, OptimisticLockingFailureException {

        boolean isStockForSale = stockService.doesStockExist(stock_symbol);
        if(isStockForSale == false){
            return "not in the offer";
        }

        //check if user bought the stock already
        Optional<Fraction> fractionOptional = Optional.ofNullable(repoFractions.findFractionByStockSymbolAndUserId(user_id, stock_symbol));
        Fraction fraction = null;


        // user owns the fraction
        if (fractionOptional.isPresent()) {

            // increase percent
            fraction = fractionOptional.get();
            BigDecimal fractionPercent = BigDecimal.valueOf(fraction.getPercent());
            fractionPercent = fractionPercent.add(requestPercent);
            fraction.setPercent(fractionPercent.doubleValue());
            try {
                repoFractions.save(fraction);
            }
            catch (OptimisticLockingFailureException e){
                return "optimistic lock failed";
            }
            catch (Exception e){
                System.out.println(e.getStackTrace());
            }
        }

        // user haven't bought the stock yet, so we create a new fraction
        else {
            // create new fraction
            fraction = new Fraction(user_id, stock_symbol, requestPercent.doubleValue());
            repoFractions.save(fraction);
        }

        // update user balance in accounting-service
        // updateBalance automatic registers transaction
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        accountingService.updateBalanceAndRegisterTransaction(user_id, fraction.getId(), TransactionType.PURCHASE, amountAfterProvision.doubleValue(), timestamp);

        return "investment accepted";
    }



    public Double getCashGainOfSingleFraction(Long fraction_id){

        Optional<Fraction> optionalFraction = repoFractions.findById(fraction_id);
        if(!optionalFraction.isPresent()){
            return null;
        }
        Fraction fraction = optionalFraction.get();
        BigDecimal fractionPercent = BigDecimal.valueOf(fraction.getPercent());
        String symbol = repoFractions.findStockSymbolById(fraction_id);
        BigDecimal currentPrice = BigDecimal.valueOf(yahooFinanceService.getCurrentPriceOfSingleStock(symbol)).setScale(2, RoundingMode.FLOOR);

        BigDecimal gain = fractionPercent.divide(new BigDecimal(100));
        gain = gain.multiply(currentPrice).setScale(2, RoundingMode.FLOOR);

        return gain.doubleValue();
    }

    public double getCashGainOfAllFractions(Long user_id){

        BigDecimal overAllGain = new BigDecimal(0);
        ArrayList<Fraction> allUserFractions = (ArrayList<Fraction>) repoFractions.findAllByUserId(user_id);
        for (Fraction f: allUserFractions
             ) {
            BigDecimal fractionPercent = BigDecimal.valueOf(f.getPercent());
            String symbol = repoFractions.findStockSymbolById(f.getId());
            BigDecimal currentPrice = BigDecimal.valueOf(yahooFinanceService.getCurrentPriceOfSingleStock(symbol)).setScale(2, RoundingMode.FLOOR);
            BigDecimal gain = fractionPercent.multiply(currentPrice).divide(new BigDecimal(100)).setScale(2, RoundingMode.FLOOR);
            overAllGain = overAllGain.add(gain);

        }

        return overAllGain.doubleValue();
    }

    public Double getFractionPercent(Long fraction_id) {

        Double percent = null;


        percent = repoFractions.findFractionPercentById(fraction_id);
        if(percent == null){
            return -1.0;
        }
        else{
            return percent;
        }

    }


}
