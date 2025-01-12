package com.bbsk.anything.naver.finance.sevice;

import com.bbsk.anything.crawler.finance.service.NaverFinanceCrawlerService;
import com.bbsk.anything.naver.finance.entity.NaverFinance;
import com.bbsk.anything.naver.finance.repository.NaverFinanceRepository;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NaverFinanceService {


    private final NaverFinanceRepository naverFinanceRepository;
    private final NaverFinanceCrawlerService naverFinanceCrawlerService;

    @Transactional
    public boolean processAndSaveCompanyPerformance(String ticker, String company, User user) {
        try {
            List<NaverFinance> naverFinances =
                    naverFinanceCrawlerService.convertPerformanceDataToEntities(
                            naverFinanceCrawlerService.fetchPerformanceDataByTicker(ticker),
                            ticker,
                            company,
                            user
                    );

            // TODO 티커 + relDate 조건으로 해당 데이터가 있으면 no save, 없으면 save
            naverFinanceRepository.saveAll(naverFinances);
            return true;
        } catch (Exception e) {
            log.error("## 기업 실적데이터를 삽입하는데 문제가 생겼습니다. {}", e.getMessage());
            return false;
        }
    }

}
