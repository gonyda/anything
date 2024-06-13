package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.javis.entity.Javis;
import com.bbsk.anything.javis.repository.JavisRepository;
import com.bbsk.anything.weather.constant.BaseDate;
import com.bbsk.anything.weather.constant.Region;
import com.bbsk.anything.weather.dto.ResponseWeatherDto;
import com.bbsk.anything.weather.service.WeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;

import static com.bbsk.anything.javis.service.JavisService.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JavisChatHandlerService {

    private final JavisRepository javisRepository;
    private final ChatGptApiService chatGptApiService;
    private final WeatherApiService weatherApiService;

    public ResponseGptChat handleWeatherChat(RequestChatByUser dto, Matcher matcher) {
        try {
            /* TODO 날씨정보 캐시 처리 */
            // 날씨 정보 API 호출
            ResponseWeatherDto weatherInfo = weatherApiService.getWeather(Region.valueOf(matcher.group(2)), // 날씨요청 지역
                    BaseDate.valueOf(matcher.group(1))); // 날씨요청 일자
            // GPT 날씨 채팅 가져오기
            ResponseChatByGpt weatherChat = chatGptApiService.getWeatherChat(dto, weatherInfo);
            // 유저 채팅 저장
            javisRepository.save(new Javis().toEntity(dto));
            // GPT 채팅 저장
            return new ResponseGptChat().toDto(
                    javisRepository.save(new Javis().toEntity(weatherChat, dto.getUser()))
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseGptChat handleGeneralChat(RequestChatByUser dto) {
        try {
            ResponseChatByGpt chat = chatGptApiService.getChat(dto);
            // 유저 채팅 저장
            javisRepository.save(new Javis().toEntity(dto));
            // GPT 채팅 저장
            Javis gptChat = javisRepository.save(new Javis().toEntity(chat, dto.getUser()));
            return new ResponseGptChat().toDto(gptChat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
