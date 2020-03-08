package com.yostocks.stocksservice.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yostocks.stocksservice.fraction.Fraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * stocks are displayed in 'Stocks' android's view
 * user chooses one to buy and chooses fraction of it
 */
@Entity
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stocks")
//@NamedQuery(name="findBySymbol", query= "select s from stocks s where s.symbol = :symbol limit 1", lockMode=PESSIMISTIC_READ)
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    protected Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "description")
    private String description;



    public Stock(String symbol,  String description) {
        this.symbol = symbol;
        this.description = description;

    }



    @Override
    public String toString() {
        return "{" +
                    "id" +":" + id +
                    ", symbol" +":" + symbol + '\'' +
                    ", description" +":" + description +
                '}';
    }
}