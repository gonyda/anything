package com.bbsk.anything.crawler.investing.constant;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InvestingPerformanceEnum {

    AAPL("https://investing.com/equities/apple-computer-inc-earnings", "earningsPageTbl", "instrumentEarningsHistory", "apple", "AAPL"),
    AMZN("https://investing.com/equities/amazon-com-inc-earnings", "earningsPageTbl", "instrumentEarningsHistory", "amazon", "AMZN"),
    MSFT("https://www.investing.com/equities/microsoft-corp-earnings", "earningsPageTbl", "instrumentEarningsHistory", "micro soft", "MSFT");

    private final String url;
    private final String firstClassName;
    private final String secondClassName;
    private final String name;
    private final String ticker;

    public static InvestingPerformanceEnum getByTicker(String ticker) {
        return Arrays.stream(InvestingPerformanceEnum.values())
                .filter(e -> StringUtils.equals(e.getTicker(), ticker))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No enum constant with ticker " + ticker));
    }
}
