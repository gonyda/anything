package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.javis.dto.*;
import com.bbsk.anything.javis.entity.Javis;
import com.bbsk.anything.javis.repository.JavisRepository;
import com.bbsk.anything.weather.constant.BaseDate;
import com.bbsk.anything.weather.constant.Region;
import com.bbsk.anything.weather.dto.ResponseWeatherDto;
import com.bbsk.anything.weather.service.WeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JavisService {

    private final JavisRepository javisRepository;
    private final ChatGptApiService chatGptApiService;
    private final WeatherApiService weatherApiService;

    @Transactional
    public ResponseGptChat callGptApi(RequestChatByUser dto) {
        // 날씨요청 채팅 여부 체크
        Matcher matcher = isChatForWeather(dto.getMessages().get(dto.getMessages().size() - 1).getContent());
        if (matcher != null) {
            ResponseWeatherDto weatherInfo;
            ResponseChatByGpt weatherChat;
            try {
                // 날씨 정보 API 호출
                weatherInfo = weatherApiService.getWeather(Region.valueOf(matcher.group(2)), // 날씨요청 지역
                                                           BaseDate.valueOf(matcher.group(1))); // 날씨요청 일자
                // GPT 날씨 채팅 가져오기
                weatherChat = chatGptApiService.getWeatherChat(dto, weatherInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }

            // 유저 채팅 저장
            javisRepository.save(new Javis().toEntity(dto));

            // GPT 채팅 저장
            Javis gptChat = javisRepository.save(new Javis().toEntity(weatherChat, dto.getUser()));

            return new ResponseGptChat().toDto(gptChat);
        }

        // API Connect
        ResponseChatByGpt chat;
        try {
            chat = chatGptApiService.getChat(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 유저 채팅 저장
        javisRepository.save(new Javis().toEntity(dto));

        // GPT 채팅 저장
        Javis gptChat = javisRepository.save(new Javis().toEntity(chat, dto.getUser()));

        return new ResponseGptChat().toDto(gptChat);
    }

    /**
     * 해당 유저의 채팅 내역 가져오기
     *
     * @param userId
     * @return
     */
    public List<ResponseGptChat> findAllByUser(String userId) {
        return javisRepository.findAllByUserUserIdOrderByChatIdAsc(userId).stream()
                .map(e -> new ResponseGptChat().toDto(e))
                .collect(toList());
    }

    /**
     * 날씨요청 채팅 여부
     * @param content
     * @return
     */
    private Matcher isChatForWeather(String content) {
        Matcher matcher = Pattern.compile(buildPatternString(BaseDate.values()) + ".*" + buildPatternString(Region.values()))
                .matcher(content);

        return StringUtils.contains(content, "날씨") && matcher.find() ?
                matcher :
                null;
    }

    /**
     * 정규식 패턴 생성
     * @param baseDates
     * @return
     */
    private String buildPatternString(BaseDate[] baseDates) {
        StringBuilder patternBuilder = new StringBuilder("(");
        for (int i = 0; i < baseDates.length; i++) {
            patternBuilder.append(baseDates[i].getDay());
            if (i < baseDates.length - 1) {
                patternBuilder.append("|");
            }
        }
        return patternBuilder.append(")").toString();
    }

    /**
     * 정규식 패턴 생성
     * @param regions
     * @return
     */
    private String buildPatternString(Region[] regions) {
        StringBuilder patternBuilder = new StringBuilder("(");
        for (int i = 0; i < regions.length; i++) {
            patternBuilder.append(regions[i].getCity());
            if (i < regions.length - 1) {
                patternBuilder.append("|");
            }
        }
        return patternBuilder.append(")").toString();
    }

    @Getter
    @ToString
    public static class ResponseGptChat {
        private String sender;
        private LocalDateTime createTime;
        private String message;
        private Long totalTokens;
        private Long maxTokens;
        private String model;

        public ResponseGptChat toDto(Javis entity) {
            this.sender = entity.getSender();
            this.createTime = entity.getCreateTime();
            this.message = entity.getMessage();
            this.totalTokens = entity.getTotalTokens();
            this.maxTokens = ChatGptModel.GPT_3_5_TURBO_1106.getTokens();
            this.model = entity.getModel();

            return this;
        }
    }
}
