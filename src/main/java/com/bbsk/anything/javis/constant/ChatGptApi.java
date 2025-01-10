package com.bbsk.anything.javis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum ChatGptApi {
    URI("https://api.openai.com/v1/chat/completions"),
    AUTHORIZATION;

    private String value;

    public void setValue(String value) {
        this.value = value;
    }
}
