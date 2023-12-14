package com.bbsk.anything.javis.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
public class ResponseChatByGpt {

    private LocalDateTime created;
    private String model;
    private Choices[] choices;
    private Usage usage;

    public void setCreated(Long created) {
        this.created = LocalDateTime.ofInstant(Instant.ofEpochSecond(created), ZoneId.of("Asia/Seoul"));
    }

    @Getter
    @ToString
    public static class Choices {
        Message message;
    }

    @Getter
    @ToString
    public static class Usage {
        private Long total_tokens;
    }
}
