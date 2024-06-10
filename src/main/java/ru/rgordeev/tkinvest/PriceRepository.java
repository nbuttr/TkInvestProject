package ru.rgordeev.tkinvest;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceRepository extends CrudRepository<Price, Long>, WithInsert<Price> {
    Iterable<Price> findAll();
    @Modifying
    @Query("DELETE FROM price")
    void deleteAllPrices();
}
