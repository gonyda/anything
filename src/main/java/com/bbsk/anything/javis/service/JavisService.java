package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.javis.dto.*;
import com.bbsk.anything.javis.entity.Javis;
import com.bbsk.anything.javis.repository.JavisRepository;
import com.bbsk.anything.utils.JavisModelHolder;
import com.bbsk.anything.javis.weather.constant.BaseDate;
import com.bbsk.anything.javis.weather.constant.Region;
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
    private final JavisChatHandlerService javisChatHandlerService;

    /*
    * 채팅 요청
    * */
    @Transactional
    public ResponseGptChat callGptApi(RequestChatByUser dto) {
        // 날씨 요청 채팅 여부 체크
        String lastMessageContent = dto.getMessages().get(dto.getMessages().size() - 1).getContent();
        Matcher matcher = isChatForWeather(lastMessageContent);

        return matcher != null ?
                javisChatHandlerService.handleWeatherChat(dto, matcher) :
                javisChatHandlerService.handleGeneralChat(dto);
    }

    /**
     * 채팅 조회
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
            this.maxTokens = ChatGptModel.getTokensByName(JavisModelHolder.get());
            this.model = entity.getModel();

            return this;
        }
    }
}
