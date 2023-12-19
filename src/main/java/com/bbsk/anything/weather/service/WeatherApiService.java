package com.bbsk.anything.weather.service;

import com.bbsk.anything.javis.dto.Message;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.bbsk.anything.weather.constant.BaseDate;
import com.bbsk.anything.weather.constant.Region;
import com.bbsk.anything.weather.constant.WeatherApi;
import com.bbsk.anything.weather.dto.ResponseWeatherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WeatherApiService {

    /**
     * TMN : 일 최저 기온
     * TMX : 일 최고 기온
     * TMP : 시간별 기온
     */
    private final static String[] CATEGORIES_TO_FILTER = {"TMP", "POP", "SKY"};
    private final static String[] TIMES = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

    public ResponseWeatherDto getWeather(String content) {
        // 정규표현식을 동적으로 생성
        String patternFormat = buildPatternString(BaseDate.values()) + ".*" + buildPatternString(Region.values());

        // 패턴 매칭
        Matcher matcher = Pattern.compile(patternFormat).matcher(content);

        if (matcher.find()) {
            Region region = Region.valueOf(matcher.group(2)); // 요청 지역
            BaseDate fcstDate = BaseDate.valueOf(matcher.group(1)); // 요청일

            try {
                ResponseWeatherDto dto = ObjectMapperHolder.INSTANCE.get()
                        .readValue(weatherApiConnect(region), ResponseWeatherDto.class);

                return dto.filterItemsByCategories(CATEGORIES_TO_FILTER, fcstDate); // 필요한 카테고리 데이터만 추출
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return null;
    }

    /**
     * 날씨예보 API 호출
     * @param region
     * @return
     */
    private String weatherApiConnect(Region region) {
        try {
            StringBuilder urlBuilder = new StringBuilder(WeatherApi.URL.getValue()); /*URL*/
            urlBuilder.append(WeatherApi.PATH.getValue());
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + WeatherApi.SERVICEKEY.getValue()); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(WeatherApi.DATATYPE.getValue(), "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "UTF-8")); /*‘21년 6월 28일발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(getNearBaseTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm"))), "UTF-8")); /*05시 발표*/
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(region.getX(), "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(region.getY(), "UTF-8")); /*예보지점의 Y 좌표값*/
            URL uri = new URL(urlBuilder.toString());

            return new RestTemplate().exchange(RequestEntity.get(uri.toURI()).build(), String.class)
                                     .getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 현재 시간 기준으로 조회시간 가져오기
     * @param time
     * @return
     */
    private String getNearBaseTime(String time) {
        int targetIndex = 0;
        for (String target : TIMES) {
            if (target.compareTo(time) <= 0) {
                targetIndex++;
                break;
            }
        }
        return TIMES[targetIndex];
    }

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
}
