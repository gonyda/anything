package com.bbsk.anything.naver.news.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api-key.naver")
public class NaverConfig {

    private String clientId;
    private String clientSecret;

    public void injectIntoEnum() {
        NaverAPI.CLIENT_ID.setValue(clientId);
        NaverAPI.CLIENT_SECRET.setValue(clientSecret);
    }
}