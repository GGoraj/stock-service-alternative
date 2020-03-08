package com.yostocks.stocksservice.fraction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/fraction")
public class FractionController {

    private FractionService fractionService;


    @Autowired
    public FractionController(FractionService fractionService) {
        this.fractionService = fractionService;
    }


    // returns double value
    @Async
    @GetMapping("/gain/single/{fraction_id}")
    public CompletableFuture getSingleFractionGain(@PathVariable Long fraction_id) {

        return CompletableFuture.completedFuture(fractionService.getCashGainOfSingleFraction(fraction_id));

    }

    // returns double value
    @Async
    @GetMapping("/gain/all/{user_id}")
    public CompletableFuture getAllFractionGain(@DecimalMin("1") @PathVariable("user_id") Long user_id) {

        return CompletableFuture.completedFuture(fractionService.getCashGainOfAllFractions(user_id));

    }

    // returns key value pair <stock_id, percent_invested> for requested user_id
    @Async
    @GetMapping("/fraction/all/{user_id}")
    public CompletableFuture getAllInvestmentsByUserId(@DecimalMin("1") @PathVariable("user_id") Long user_id) {

        // GET ALL USER FRACTIONS
        Collection<Fraction> allFractions = fractionService.getAllFractionsByUserId(user_id);

        // if user doesnt owe any fractions
        if (allFractions.equals(null) || allFractions.isEmpty()) {

            return CompletableFuture.completedFuture(Collections.singletonMap("result", "no fractions yet"));
        }

        // CHECK STOCK SYMBOL OF EACH FRACTION
        HashMap<Long, String> mapFractionIdToStockSymbol = fractionService.getStockSymbolsByAllFractions(allFractions);

        // PREPARE LIST OF INVESTMENTS
        ArrayList<InvestmentModel> investments = new ArrayList<>();
        for (Fraction f : allFractions
        ) {
            Long fraction_id = f.getId();
            String stock_symbol = mapFractionIdToStockSymbol.get(fraction_id);
            double percent = f.getPercent();
            investments.add(new InvestmentModel(fraction_id, stock_symbol, percent));
        }

        return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("result", investments), HttpStatus.OK));

    }


    // 2. user wants to buy a fraction of stock in order to keep his own as an investment
    // this method checks if there is any fraction of stock 's' bought already and if so increments the value
    // of aforementioned fraction as requested by user
    @Async
    @PostMapping("/buy")
    public CompletableFuture buyFraction(@Valid @RequestBody BuyFractionRequestModel requestModel) {

        String result = fractionService.buyFraction(requestModel);
        if (result == null) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "yahoo currently service not available"), HttpStatus.INTERNAL_SERVER_ERROR));
        }

        if(result.equals("not in the offer") ){
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "stock " + result), HttpStatus.NOT_FOUND));
        }

        if (result.equals("insufficient credit")) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "insufficient credit"), HttpStatus.NOT_ACCEPTABLE));

        } else {

            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", result), HttpStatus.ACCEPTED));

        }

    }

    // sell requested stock percentage
    @Async
    @PostMapping("/sell")
    public CompletableFuture getFractionPercent(@Valid @RequestBody SellFractionRequestModel requestModel) {

        double result = fractionService.sellFractionByPercentage(requestModel);
        // is percent value valid
        if (result == 0) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "invalid percent value"), HttpStatus.BAD_REQUEST));
        }

        if (result == -1) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "no fraction to proceed with"), HttpStatus.BAD_REQUEST));
        }

        if (result == -2) {
            // if requested percentage is > then owned by the user
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "requested percentage too high "), HttpStatus.CONFLICT));
        }

        if (result == -3) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "accounting: fail"), HttpStatus.BAD_REQUEST));
        }


        return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "stock percentage sold"), HttpStatus.OK));


    }

    @Async
    @GetMapping("fraction/percent/{fraction_id}")
    public CompletableFuture getFractionPercent(@PathVariable Long fraction_id) {

        Double fractionPercent = fractionService.getFractionPercent(fraction_id);
        if (fractionPercent == -1.0) return CompletableFuture
                .completedFuture(new ResponseEntity<>(Collections.singletonMap("response", "fraction not found"), HttpStatus.NOT_FOUND));

        return CompletableFuture.completedFuture(new ResponseEntity<>(Collections.singletonMap("response", fractionPercent), HttpStatus.OK));

    }


}
