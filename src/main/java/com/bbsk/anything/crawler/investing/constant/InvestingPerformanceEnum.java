package com.bbsk.anything.crawler.investing.constant;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum InvestingPerformanceEnum {

    AAPL("https://investing.com/equities/apple-computer-inc-earnings", "earningsPageTbl", "instrumentEarningsHistory", "apple"),
    AMZN("https://investing.com/equities/amazon-com-inc-earnings", "earningsPageTbl", "instrumentEarningsHistory", "amazon");

    private final String url;
    private final String firstClassName;
    private final String secondClassName;
    private final String name;

}
