package com.bbsk.anything.crawler.investing.repository;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestingPerformanceRepository extends JpaRepository<InvestingPerformance, Long> {
    InvestingPerformance findByCompanyAndRelDate(String company, String relDate);
}
