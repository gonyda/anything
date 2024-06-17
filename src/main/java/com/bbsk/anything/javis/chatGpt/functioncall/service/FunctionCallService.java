package com.bbsk.anything.javis.chatGpt.functioncall.service;

import com.bbsk.anything.javis.chatGpt.functioncall.dto.RequestFunctionCall;
import com.bbsk.anything.javis.chatGpt.functioncall.dto.weather.Functions;
import com.bbsk.anything.javis.constant.ChatGptApi;
import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.javis.chatGpt.functioncall.dto.ResponseFunctionCall;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
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
public class FunctionCallService {

    /**
     * function call 함수 요청
     * @param requestFunctionCall
     * @return
     * @throws JsonProcessingException
     */
    public ResponseFunctionCall getResponseFunctionCall(RequestFunctionCall requestFunctionCall) throws JsonProcessingException {
        return ObjectMapperHolder.INSTANCE.get()
                .readValue(functionCallApi(requestFunctionCall).getBody(), ResponseFunctionCall.class);
    }

    /**
     * function call을 사용한 채팅 요청
     * @param requestFunctionCall
     * @return
     * @throws JsonProcessingException
     */
    public ResponseChatByGpt getResponseFunctionCallChat(RequestFunctionCall requestFunctionCall) throws JsonProcessingException {
        return ObjectMapperHolder.INSTANCE.get().readValue(functionCallApi(requestFunctionCall).getBody()
                                                            , ResponseChatByGpt.class);
    }

    /**
     * ChatGPT function call API 호출
     * @param dto
     * @return
     */
    public ResponseEntity<String> functionCallApi(RequestFunctionCall dto) {
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

    /**
     * function call 사용 할 함수 정의
     * @param dto
     * @return
     */
    public RequestFunctionCall setRequestFunctionCall(RequestChatByUser dto) {
        List<Functions> functions = new ArrayList<>();
        functions.add(new Functions());

        List<Object> messages = new ArrayList<>();
        messages.add(dto.getMessages().get(dto.getMessages().size() -1));

        return RequestFunctionCall.builder()
                .messages(messages)
                .functions(functions)
                .build();
    }
}
