package com.yostocks.stocksservice.fraction;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Collection;

@Repository
public interface IFractionRepository extends CrudRepository<Fraction, Long> {

    // which expects 'User' property in the project
    @Query(value = "SELECT * from Fractions f where f.user_id = ?1", nativeQuery = true)
    Collection<Fraction> findAllByUserId(Long user_id);


    @Query(value = "select f.* from fractions f where f.user_id = :user_id and f.stock_symbol = :stock_symbol", nativeQuery = true)
    Fraction findFractionByStockSymbolAndUserId(Long user_id, String stock_symbol);


    @Query(value = "select f.stock_symbol from fractions f where f.id = :fraction_id", nativeQuery = true)
    String findStockSymbolById(Long fraction_id);



    @Query(value = "select f.percent from fractions f where f.id = :fraction_id", nativeQuery = true)
    Double findFractionPercentById(Long fraction_id);



}