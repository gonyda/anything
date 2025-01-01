package com.bbsk.anything.exchangeRate.constant;

import lombok.Getter;

@Getter
public enum ApiConfig {
    /* API 서비스 중단 */
    API_URL("https://quotation-api-cdn.dunamu.com"),
    API_URL_PATH("/v1/forex/recent"),
    PARAMS(new String[]{"FRX.KRWUSD", "FRX.KRWJPY", "FRX.KRWEUR"}),

    API_URL_V2("https://m.search.naver.com"),
    API_URL_PATH_V2("/p/csearch/content/qapirender.nhn"),
    PARAMS_V2(new String[]{"USD","JPY"});

    private final String value;
    private final String[] arrValue;

    ApiConfig(String value) {
        this.value = value;
        this.arrValue = null;
    }

    ApiConfig(String[] arrValue) {
        this.value = null;
        this.arrValue = arrValue;
    }
}
