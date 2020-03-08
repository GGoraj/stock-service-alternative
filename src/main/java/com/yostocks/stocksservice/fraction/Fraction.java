package com.yostocks.stocksservice.fraction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Fraction is a fraction of a Stock.
 * User chooses to buy a chosen Fraction
 * And This entity refers to the chosen fraction.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "fractions")
public class Fraction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "stock_symbol")
    private String stock_symbol;

    @Column(name = "percent")
    private double percent;


    @Version
    @Column(name = "version")
    private long version;



    // constructor
    public Fraction(Long user_id, String stock_symbol, double percent) {

        this.user_id = user_id;
        this.percent = percent;
        this.stock_symbol = stock_symbol;
    }


}