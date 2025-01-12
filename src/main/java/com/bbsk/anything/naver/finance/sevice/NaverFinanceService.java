package com.bbsk.anything.naver.finance.sevice;

import com.bbsk.anything.crawler.utils.SeleniumUtils;
import com.bbsk.anything.naver.finance.entity.NaverFinance;
import com.bbsk.anything.naver.finance.repository.NaverFinanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NaverFinanceService {

    private static final String NAVER_FINANCE_URL_1 = "https://m.stock.naver.com/worldstock/stock/";
    private static final String NAVER_FINANCE_URL_2 = ".O/finance/ratios/quarter";
    private static final String TARGET_TABLE_CLASS_NAME = "TableFixed_tableArea__B9od-";
    private static final String TAG_NAME = "tr";
    private static final Set<String> ROW_TYPES = Set.of("EPS", "매출액", "당기순이익", "순이익마진율", "기간");

    private final NaverFinanceRepository naverFinanceRepository;

    @Transactional
    public boolean processAndSaveCompanyPerformance(String ticker, String company) {
        try {
            List<List<String>> performanceData = fetchPerformanceDataByTicker(ticker);
            List<NaverFinance> naverFinances = convertPerformanceDataToEntities(performanceData, ticker, company);

            // TODO 티커 + relDate 조건으로 해당 데이터가 있으면 no save, 없으면 save
            naverFinanceRepository.saveAll(naverFinances);
            return true;
        } catch (Exception e) {
            log.error("## 기업 실적데이터를 삽입하는데 문제가 생겼습니다. {}", e.getMessage());
            return false;
        }
    }

    private List<List<String>> fetchPerformanceDataByTicker(String ticker) {
        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        try {
            List<WebElement> rows = fetchTableRows(ticker, chromeDriver);
            return extractPerformanceRows(rows);
        } finally {
            SeleniumUtils.close(chromeDriver);
        }
    }

    private List<WebElement> fetchTableRows(String ticker, WebDriver chromeDriver) {
        WebElement parentElement = SeleniumUtils.getParentElement(buildTickerUrl(ticker), TARGET_TABLE_CLASS_NAME, chromeDriver);
        return SeleniumUtils.getChildElementsByTagName(parentElement, TAG_NAME);
    }

    private List<List<String>> extractPerformanceRows(List<WebElement> rows) {
        return rows.stream()
                .map(e -> Arrays.stream(e.getText().replace("\n", " ").split(" ")).toList())
                .filter(r -> isRelevantRowType(r.get(0)))
                .toList();
    }

    private String buildTickerUrl(String ticker) {
        return StringUtils.join(NAVER_FINANCE_URL_1, ticker, NAVER_FINANCE_URL_2);
    }

    private boolean isRelevantRowType(String rowType) {
        return ROW_TYPES.contains(rowType);
    }

    private List<NaverFinance> convertPerformanceDataToEntities(List<List<String>> performanceData, String ticker, String company) {
        return mapToFinanceEntities(
                extractColumnValues(performanceData, "기간"),
                extractColumnValues(performanceData, "EPS"),
                extractColumnValues(performanceData, "매출액"),
                extractColumnValues(performanceData, "당기순이익"),
                extractColumnValues(performanceData, "순이익마진율"),
                ticker,
                company);
    }

    private List<String> extractColumnValues(List<List<String>> data, String rowPrefix) {
        return data.stream()
                .filter(row -> rowPrefix.equals(row.get(0)))
                .findFirst()
                .map(row -> row.subList(1, row.size()))
                .orElse(List.of());
    }

    private List<NaverFinance> mapToFinanceEntities(
            List<String> dates, List<String> eps, List<String> revenues,
            List<String> netIncomes, List<String> profitMargins, String ticker, String company) {
        List<NaverFinance> entities = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            entities.add(NaverFinance.builder()
                    .ticker(ticker)
                    .company(company)
                    .relDate(dates.get(i))
                    .eps(getValueByIndex(eps, i))
                    .salesRevenue(getValueByIndex(revenues, i))
                    .netIncome(getValueByIndex(netIncomes, i))
                    .netProfitMargin(getValueByIndex(profitMargins, i))
                    .build());
        }
        return entities;
    }

    private String getValueByIndex(List<String> values, int index) {
        return index < values.size() ? values.get(index) : null;
    }
}
