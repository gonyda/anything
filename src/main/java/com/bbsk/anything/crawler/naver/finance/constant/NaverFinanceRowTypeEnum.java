package com.bbsk.anything.crawler.naver.finance.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NaverFinanceRowTypeEnum {

    DATE("기간"),
    EPS("EPS"),
    REVENUE("매출액"),
    NET_INCOME("당기순이익"),
    PROFIT_MARGIN("순이익마진율");

    private final String columnName;
}
