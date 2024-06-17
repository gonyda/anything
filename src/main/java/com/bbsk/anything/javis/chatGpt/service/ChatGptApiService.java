package com.bbsk.anything.javis.chatGpt.service;

import com.bbsk.anything.javis.constant.ChatGptApi;
import com.bbsk.anything.javis.dto.*;
import com.bbsk.anything.javis.chatGpt.functioncall.dto.RequestFunctionCall;
import com.bbsk.anything.javis.chatGpt.functioncall.dto.ResponseFunctionCall;
import com.bbsk.anything.javis.chatGpt.functioncall.service.FunctionCallService;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.bbsk.anything.javis.weather.dto.ResponseWeatherDto;
import com.bbsk.anything.javis.weather.service.WeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptApiService {

    private final FunctionCallService functionCallService;
    private final WeatherApiService weatherApiService;

    /**
     * chat GPT 일반채팅
     * @param dto
     * @return
     */
    public ResponseChatByGpt getGeneralChat(RequestChatByUser dto) throws JsonProcessingException {
        RequestEntity<RequestApiDto> request = RequestEntity
                .post(getUri()) // http method (get, post, ...)
                .header("Authorization", ChatGptApi.AUTHORIZATION.getValue())
                .body(getRequestApiDto(dto));

        return ObjectMapperHolder.INSTANCE.get().readValue(new RestTemplate().exchange(request, String.class).getBody()
                                                            , ResponseChatByGpt.class);
    }

    /**
     * chat GPT 날씨 조회 채팅
     * @param dto
     * @param weatherInfo
     * @return
     */
    public ResponseChatByGpt getWeatherChat(RequestChatByUser dto, ResponseWeatherDto weatherInfo) throws JsonProcessingException {
        // function call 함수 세팅
        RequestFunctionCall requestFunctionCall = functionCallService.setRequestFunctionCall(dto);

        // function call 함수 요청
        ResponseFunctionCall responseFunctionCall = functionCallService.getResponseFunctionCall(requestFunctionCall);

        if (responseFunctionCall != null) {
            // function call 함수에 넘길 데이터 세팅 (날씨 API 데이터)
            weatherApiService.setRequestDataForFunctionCall(requestFunctionCall, responseFunctionCall, weatherInfo);
        }

        // function call 함수와 데이터를 넘겨 채팅 요청
        return functionCallService.getResponseFunctionCallChat(requestFunctionCall);
    }

    /**
     * 요청 dto 세팅
     * @param dto
     * @return
     */
    private RequestApiDto getRequestApiDto(RequestChatByUser dto) {
        return RequestApiDto.builder()
                .model(dto.getModel())
                .messages(dto.getMessages())
                .build();
    }

    /**
     * uri 세팅
     * @return
     */
    private URI getUri() {
        return UriComponentsBuilder.fromUriString (ChatGptApi.URI.getValue())
                .encode()
                .build()
                .toUri();
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
