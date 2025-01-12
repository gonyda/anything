package com.bbsk.anything.crawler.naver.finance.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NaverFinanceEnum {

    NAVER_FINANCE_URL_1("https://m.stock.naver.com/worldstock/stock/"),
    NAVER_FINANCE_URL_2(".O/finance/ratios/quarter"),
    TARGET_TABLE_CLASS_NAME("TableFixed_tableArea__B9od-"),
    TAG_NAME("tr");

    private final String value;
}
