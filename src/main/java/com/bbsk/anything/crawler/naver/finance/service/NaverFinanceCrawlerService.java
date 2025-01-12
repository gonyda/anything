package com.bbsk.anything.crawler.naver.finance.service;

import com.bbsk.anything.crawler.naver.finance.constant.NaverFinanceCrawlerEnum;
import com.bbsk.anything.crawler.naver.finance.constant.NaverFinanceRowTypeEnum;
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
    private static final Set<NaverFinanceRowTypeEnum> ROW_TYPES =
            Set.of(NaverFinanceRowTypeEnum.DATE,
                    NaverFinanceRowTypeEnum.EPS,
                    NaverFinanceRowTypeEnum.REVENUE,
                    NaverFinanceRowTypeEnum.NET_INCOME,
                    NaverFinanceRowTypeEnum.PROFIT_MARGIN);

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
                extractColumnValues(performanceData, NaverFinanceRowTypeEnum.DATE),
                extractColumnValues(performanceData, NaverFinanceRowTypeEnum.EPS),
                extractColumnValues(performanceData, NaverFinanceRowTypeEnum.REVENUE),
                extractColumnValues(performanceData, NaverFinanceRowTypeEnum.NET_INCOME),
                extractColumnValues(performanceData, NaverFinanceRowTypeEnum.PROFIT_MARGIN),
                ticker,
                company);
    }

    private List<WebElement> fetchTableRows(String ticker, WebDriver chromeDriver) {
        WebElement parentElement = SeleniumUtils.getParentElement(buildTickerUrl(ticker), NaverFinanceCrawlerEnum.TARGET_TABLE_CLASS_NAME.getValue(), chromeDriver);
        return SeleniumUtils.getChildElementsByTagName(parentElement, NaverFinanceCrawlerEnum.TAG_NAME.getValue());
    }

    private List<List<String>> extractPerformanceRows(List<WebElement> rows) {
        return rows.stream()
                .map(e -> Arrays.stream(e.getText().replace("\n", " ").split(" ")).toList())
                .filter(r -> isRowType(r.get(0)))
                .toList();
    }

    private String buildTickerUrl(String ticker) {
        return StringUtils.join(NaverFinanceCrawlerEnum.NAVER_FINANCE_URL_1.getValue(), ticker, NaverFinanceCrawlerEnum.NAVER_FINANCE_URL_2.getValue());
    }

    private boolean isRowType(String rowType) {
        return ROW_TYPES.stream()
                .map(NaverFinanceRowTypeEnum::getColumnName)
                .anyMatch(rowType::equals);
    }

    private List<String> extractColumnValues(List<List<String>> data, NaverFinanceRowTypeEnum columnType) {
        return data.stream()
                .filter(row -> columnType.getColumnName().equals(row.get(0)))
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
