package com.bbsk.anything.news.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum NaverAPI {
    CLIENT_ID(""),
    CLIENT_SECRET(""),
    URL("https://openapi.naver.com"),
    PATH ("/v1/search/news.json");

    private String value;
}
