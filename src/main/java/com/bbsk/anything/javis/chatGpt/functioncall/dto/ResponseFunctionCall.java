package com.bbsk.anything.javis.chatGpt.functioncall.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseFunctionCall {

    private Choices[] choices;

    @Getter
    @ToString
    public static class Choices {
        private Message message;

        @Getter
        @ToString
        public static class Message{
            private String role;
            private String content;
            private FunctionCall function_call;

            @Getter
            @ToString
            public static class FunctionCall {
                private String name;
                private String arguments;
            }
        }
    }
}
