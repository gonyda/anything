package com.bbsk.anything.naver.news.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum NaverAPI {
    CLIENT_ID,
    CLIENT_SECRET,
    URL("https://openapi.naver.com"),
    PATH("/v1/search/news.json");

    private String value;

    public void setValue(String value) {
        this.value = value;
    }
}
