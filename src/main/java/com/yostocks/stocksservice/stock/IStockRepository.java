package com.yostocks.stocksservice.stock;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface IStockRepository extends CrudRepository<Stock, Long> {

    @Query(value="select * from stocks s where s.symbol = :symbol limit 1", nativeQuery = true)
    Stock findBySymbol(String symbol);


}