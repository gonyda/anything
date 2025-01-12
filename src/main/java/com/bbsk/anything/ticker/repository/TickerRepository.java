package com.bbsk.anything.ticker.repository;

import com.bbsk.anything.ticker.entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TickerRepository extends JpaRepository<Ticker, Long> {

    List<Ticker> findTop10ByTickerNameContainingOrderByTickerId(String ticker);
}
