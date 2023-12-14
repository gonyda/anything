package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.constant.ChatGptApi;
import com.bbsk.anything.javis.dto.Message;
import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ChatGptApiService {

    /**
     * @param dto
     * @return
     */
    public ResponseChatByGpt getChat(RequestChatByUser dto) {
        try {
            return ObjectMapperHolder.INSTANCE.get()
                    .readValue(chatGptApiConnect(dto).getBody(), ResponseChatByGpt.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param dto
     * @return
     */
    private ResponseEntity<String> chatGptApiConnect(RequestChatByUser dto) {
        URI uri = UriComponentsBuilder.fromUriString (ChatGptApi.URI.getValue())
                .encode()
                .build()
                .toUri();

        RequestApiDto requestApiDto = RequestApiDto.builder()
                .model(dto.getModel())
                .messages(dto.getMessages())
                .build();

        RequestEntity<RequestApiDto> request = RequestEntity
                .post(uri) // http method (get, post, ...)
                .header("Authorization", ChatGptApi.AUTHORIZATION.getValue())
                .body(requestApiDto);

        return new RestTemplate().exchange(request, String.class);
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RequestApiDto {
        private String model;
        private Message[] messages;
    }
}
