package com.bbsk.anything.utils;

import com.bbsk.anything.javis.constant.ChatGptModel;

public enum JavisModelHolder {

    JAVAIS_MODEL;

    private final String javisModel;

    JavisModelHolder() {
        this.javisModel = create();
    }

    public String get() {
        return this.javisModel;
    }

    private static String create() {
        return ChatGptModel.GPT_4_o_2024_05_13.getName();
    }
}
