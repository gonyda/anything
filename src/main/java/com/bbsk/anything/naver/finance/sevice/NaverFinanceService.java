package com.bbsk.anything.naver.finance.sevice;

import com.bbsk.anything.crawler.finance.service.NaverFinanceCrawlerService;
import com.bbsk.anything.naver.finance.entity.NaverFinance;
import com.bbsk.anything.naver.finance.repository.NaverFinanceRepository;
import com.bbsk.anything.user.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

            // 티커와 날짜 조건으로 중복 데이터가 존재하는지 확인
            List<NaverFinance> newFinancesToSave = new ArrayList<>();
            for (NaverFinance finance : naverFinances) {
                boolean exists = naverFinanceRepository.existsByTickerAndRelDate(finance.getTicker(), finance.getRelDate());
                if (!exists) {
                    newFinancesToSave.add(finance);
                }
            }

            // 새로운 데이터만 저장
            if (!newFinancesToSave.isEmpty()) {
                naverFinanceRepository.saveAll(newFinancesToSave);
            }
            return true;
        } catch (Exception e) {
            log.error("## 기업 실적데이터를 삽입하는데 문제가 생겼습니다. {}", e.getMessage());
            return false;
        }
    }

    public List<NaverPerformanceResponseDto> getPerformance(User user) {
        List<NaverFinance> recent5MonthPerformanceByUser = naverFinanceRepository.findRecent5MonthPerformanceByUser(user.getUserId());
        List<String> companyList = recent5MonthPerformanceByUser
                .stream()
                .map(NaverFinance::getCompany)
                .distinct()
                .toList();

        List<NaverPerformanceResponseDto> responseDtoList = new ArrayList<>();

        companyList.forEach(company -> {
            NaverPerformanceResponseDto.NaverPerformanceResponseDtoBuilder builder = NaverPerformanceResponseDto.builder();
            builder.company(company);
            builder.performanceList(recent5MonthPerformanceByUser.stream()
                    .filter(performance -> {
                        return company.equals(performance.getCompany());
                    })
                    .toList());
            responseDtoList.add(builder.build());
        });

        return responseDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @ToString
    public static class NaverPerformanceResponseDto {
        private String company;
        private List<NaverFinance> performanceList = new ArrayList<>();
    }
}
