package com.bbsk.anything.naver.finance.repository;

import com.bbsk.anything.naver.finance.entity.NaverFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NaverFinanceRepository extends JpaRepository<NaverFinance, Long> {

    @Query(value = """
            SELECT nf.*
            FROM (
                     SELECT nf.*,
                            ROW_NUMBER() OVER (PARTITION BY nf.ticker ORDER BY nf.rel_date DESC) AS row_num
                     FROM naver_finance nf
                     WHERE nf.user_id = :userId
                 ) nf
            WHERE nf.row_num <= 5
            ORDER BY nf.ticker, nf.rel_date DESC;
            """, nativeQuery = true)
    List<NaverFinance> findRecent5MonthPerformanceByUser(@Param("userId") String userId);
}
