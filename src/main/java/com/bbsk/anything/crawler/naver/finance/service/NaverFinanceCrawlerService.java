package com.bbsk.anything.crawler.naver.finance.service;

import com.bbsk.anything.crawler.naver.finance.constant.NaverFinanceEnum;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import com.bbsk.anything.naver.finance.entity.NaverFinance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverFinanceCrawlerService {

    // 크롤링 데이터에서 필요한 Row
    private static final Set<String> ROW_TYPES = Set.of("EPS", "매출액", "당기순이익", "순이익마진율", "기간");

    // 크롤링 데이터 조회
    public List<List<String>> fetchPerformanceDataByTicker(String ticker) {
        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        try {
            List<WebElement> rows = fetchTableRows(ticker, chromeDriver);
            return extractPerformanceRows(rows);
        } finally {
            SeleniumUtils.close(chromeDriver);
        }
    }

    public List<NaverFinance> convertPerformanceDataToEntities(List<List<String>> performanceData, String ticker, String company) {
        return mapToFinanceEntities(
                extractColumnValues(performanceData, "기간"),
                extractColumnValues(performanceData, "EPS"),
                extractColumnValues(performanceData, "매출액"),
                extractColumnValues(performanceData, "당기순이익"),
                extractColumnValues(performanceData, "순이익마진율"),
                ticker,
                company);
    }

    private List<WebElement> fetchTableRows(String ticker, WebDriver chromeDriver) {
        WebElement parentElement = SeleniumUtils.getParentElement(buildTickerUrl(ticker), NaverFinanceEnum.TARGET_TABLE_CLASS_NAME.getValue(), chromeDriver);
        return SeleniumUtils.getChildElementsByTagName(parentElement, NaverFinanceEnum.TAG_NAME.getValue());
    }

    private List<List<String>> extractPerformanceRows(List<WebElement> rows) {
        return rows.stream()
                .map(e -> Arrays.stream(e.getText().replace("\n", " ").split(" ")).toList())
                .filter(r -> isRowType(r.get(0)))
                .toList();
    }

    private String buildTickerUrl(String ticker) {
        return StringUtils.join(NaverFinanceEnum.NAVER_FINANCE_URL_1.getValue(), ticker, NaverFinanceEnum.NAVER_FINANCE_URL_2.getValue());
    }


    private boolean isRowType(String rowType) {
        return ROW_TYPES.contains(rowType);
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
