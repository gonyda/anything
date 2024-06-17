package com.bbsk.anything.exchangeRate.constant;

import lombok.Getter;

@Getter
public enum ApiConfig {
    API_URL("https://quotation-api-cdn.dunamu.com"),
    API_URL_PATH("/v1/forex/recent"),
    PARAMS(new String[]{"FRX.KRWUSD", "FRX.KRWJPY", "FRX.KRWEUR"});

    private final String value;
    private final Object[] arrValue;

    ApiConfig(String value) {
        this.value = value;
        this.arrValue = null;
    }

    ApiConfig(String[] arrValue) {
        this.value = null;
        this.arrValue = arrValue;
    }
}
