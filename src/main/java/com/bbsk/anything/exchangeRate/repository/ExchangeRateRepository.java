package com.bbsk.anything.exchangeRate.repository;

import com.bbsk.anything.exchangeRate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query(value = "SELECT e.* FROM exchange_rate e " +
            "JOIN ( " +
            "    SELECT code, MAX(CONCAT(date, ' ', time)) as max_date_time " +
            "    FROM exchange_rate " +
            "    WHERE code IN (:codes) " +
            "    GROUP BY code " +
            ") sub ON e.code = sub.code AND CONCAT(e.date, ' ', e.time) = sub.max_date_time " +
            "WHERE e.code IN (:codes)", nativeQuery = true)
    List<ExchangeRate> findLatestExchangeRatesByCodes(@Param("codes") List<String> codes);
}
