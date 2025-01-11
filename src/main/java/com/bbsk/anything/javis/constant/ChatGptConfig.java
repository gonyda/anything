package com.bbsk.anything.javis.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api-key.chat-gpt")
public class ChatGptConfig {


    private String authorization;

    public void injectIntoEnum() {
        ChatGptApi.AUTHORIZATION.setValue(authorization);
    }
}
