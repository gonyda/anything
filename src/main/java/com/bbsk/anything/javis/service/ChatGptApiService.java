package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.constant.ChatGptApi;
import com.bbsk.anything.javis.dto.*;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.bbsk.anything.weather.dto.ResponseWeatherDto;
import com.bbsk.anything.weather.service.WeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptApiService {

    private final WeatherApiService weatherApiService;

    /**
     * chat GPT 일반채팅
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
     * chat GPT 일반채팅 API 호출
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

    /**
     * ChatGPT function call
     *
     * @param dto
     * @param weatherInfo
     * @return
     */
    public ResponseFunctionCall getWeather(RequestFunctionCall requestFunctionCall, ResponseWeatherDto weatherInfo) {
        try {
            ResponseFunctionCall responseFunctionCall = ObjectMapperHolder.INSTANCE.get()
                    .readValue(functionCallApi(requestFunctionCall).getBody(), ResponseFunctionCall.class);

            System.out.println("requestFunctionCall = " + requestFunctionCall.toString());
            System.out.println("responseFunctionCall = " + responseFunctionCall.toString());
            System.out.println("weatherInfo = " + weatherInfo.toString());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    /**
     * ChatGPT function call API 호출
     * @param dto
     * @return
     */
    private ResponseEntity<String> functionCallApi(RequestFunctionCall dto) {
        URI uri = UriComponentsBuilder.fromUriString (ChatGptApi.URI.getValue())
                .encode()
                .build()
                .toUri();

        RequestEntity<RequestFunctionCall> request = RequestEntity
                .post(uri) // http method (get, post, ...)
                .header("Authorization", ChatGptApi.AUTHORIZATION.getValue())
                .body(dto);

        return new RestTemplate().exchange(request, String.class);
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RequestApiDto {
        private String model;
        private List<Message> messages;
    }
}
