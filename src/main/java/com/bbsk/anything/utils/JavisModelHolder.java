package com.bbsk.anything.utils;

import com.bbsk.anything.javis.constant.ChatGptModel;

public class JavisModelHolder {

    private static final String JAVIS_MODEL = create();

    public static String get() {
        return JAVIS_MODEL;
    }

    private static String create() {
        return ChatGptModel.GPT_4_o_2024_05_13.getName();
    }
}
