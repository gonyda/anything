package com.bbsk.anything.javis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public enum ChatGptModel {

    GPT_3_5_TURBO("gpt-3.5-turbo", 4096L),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106", 16385L),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125", 16385L),
    GPT_4_o_2024_05_13("gpt-4o-2024-05-13", 128000L);

    private String name;
    private Long tokens;

    public static Long getTokensByName(String name) {
        for (ChatGptModel model : ChatGptModel.values()) {
            if (model.getName().equalsIgnoreCase(name)) {
                return model.getTokens();
            }
        }
        return null; // or throw an exception if the name is not found
    }
}
