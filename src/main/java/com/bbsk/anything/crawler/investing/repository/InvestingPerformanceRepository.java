package com.bbsk.anything.crawler.investing.repository;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestingPerformanceRepository extends JpaRepository<InvestingPerformance, Long> {
    InvestingPerformance findByCompanyAndPeriodEnd(String company, String relDate);

    List<InvestingPerformance> findTop8ByTickerOrderByIdDesc(String upperCase);
}
