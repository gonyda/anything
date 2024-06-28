package com.bbsk.anything.crawler.investing.repository;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestingPerformanceRepository extends JpaRepository<InvestingPerformance, Long> {
    InvestingPerformance findByCompanyAndPeriodEnd(String company, String relDate);

    List<InvestingPerformance> findTop8ByTickerOrderByIdDesc(String upperCase);

    @Query(value = "SELECT * FROM (" +
            "    SELECT ip.*, " +
            "           ROW_NUMBER() OVER (PARTITION BY ip.ticker ORDER BY ip.id DESC) as num " +
            "      FROM investing_performance ip" +
            ") A " +
            "WHERE A.num <= 8",
            nativeQuery = true)
    List<InvestingPerformance> getTop8PerformanceGroupByTicker();
}
