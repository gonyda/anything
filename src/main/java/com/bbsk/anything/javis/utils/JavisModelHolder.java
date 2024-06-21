package com.bbsk.anything.javis.utils;

import com.bbsk.anything.javis.constant.ChatGptModel;

/*
* Javis에 사용되는 AI 모델 정의
* */
public class JavisModelHolder {

    private static final String JAVIS_MODEL = create();

    public static String get() {
        return JAVIS_MODEL;
    }

    private static String create() {
        return ChatGptModel.GPT_3_5_TURBO_0125.getName();
    }
}
