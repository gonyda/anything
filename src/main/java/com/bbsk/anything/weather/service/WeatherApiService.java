package com.bbsk.anything.weather.service;

import com.bbsk.anything.utils.ObjectMapperHolder;
import com.bbsk.anything.weather.constant.BaseDate;
import com.bbsk.anything.weather.constant.Region;
import com.bbsk.anything.weather.constant.WeatherApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WeatherApiService {

    public ResponseWeatherDto getWeather(String content) {
        ResponseWeatherDto dto = new ResponseWeatherDto();
        // 정규표현식을 동적으로 생성
        String cityPattern = buildPatternString(Region.values());
        String datePattern = buildPatternString(BaseDate.values());
        String patternFormat = datePattern + ".*" + cityPattern;
        Pattern pattern = Pattern.compile(patternFormat);

        // 패턴 매칭 테스트
        Matcher matcher = pattern.matcher(content);
        boolean match = matcher.find();

        // 결과 출력
        if (match) {
            String city = matcher.group(2); // 첫 번째 그룹은 도시명
            String date = matcher.group(1); // 두 번째 그룹은 날짜

            try {
                dto = ObjectMapperHolder.INSTANCE.get()
                    .readValue(weatherApiConnect(Region.valueOf(city), BaseDate.valueOf(date)).getBody(), ResponseWeatherDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return dto;
    }

    private ResponseEntity<String> weatherApiConnect(Region region, BaseDate day) {
        /*URI uri = UriComponentsBuilder.fromUriString (WeatherApi.URL.getValue())
                .path(WeatherApi.PATH.getValue())
                .queryParam("serviceKey", WeatherApi.SERVICEKEY.getValue())
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "1000")
                .queryParam("dataType", WeatherApi.DATATYPE.getValue())
                .queryParam("base_date", day.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .queryParam("base_time", getNearBaseTime(day.getLocalDateTime().format(DateTimeFormatter.ofPattern("HHmm"))))
                .queryParam("nx", region.getX())
                .queryParam("ny", region.getY())
                .encode()
                .build()
                .toUri();*/

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        try {
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + WeatherApi.SERVICEKEY.getValue()); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(WeatherApi.DATATYPE.getValue(), "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(day.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")), "UTF-8")); /*‘21년 6월 28일발표*/
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(getNearBaseTime(day.getLocalDateTime().format(DateTimeFormatter.ofPattern("HHmm"))), "UTF-8")); /*05시 발표*/
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(region.getX(), "UTF-8")); /*예보지점의 X 좌표값*/
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(region.getY(), "UTF-8")); /*예보지점의 Y 좌표값*/
            URL uri = new URL(urlBuilder.toString());
            RequestEntity<Void> request = RequestEntity
                    .get(uri.toURI()) // http method (get, post, ...)
                    .build();

            return new RestTemplate().exchange(request, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getNearBaseTime(String time) {
        String[] timeList = {"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

        int targetIndex = 0;
        for (String target : timeList) {
            if (target.compareTo(time) <= 0) {
                targetIndex++;
                break;
            }
        }

        return timeList[targetIndex];
    }

    private String buildPatternString(BaseDate[] baseDates) {
        StringBuilder patternBuilder = new StringBuilder("(");

        for (int i = 0; i < baseDates.length; i++) {
            patternBuilder.append(baseDates[i].getDay());
            if (i < baseDates.length - 1) {
                patternBuilder.append("|");
            }
        }

        patternBuilder.append(")");

        return patternBuilder.toString();
    }

    private String buildPatternString(Region[] regions) {
        StringBuilder patternBuilder = new StringBuilder("(");

        for (int i = 0; i < regions.length; i++) {
            patternBuilder.append(regions[i].getCity());
            if (i < regions.length - 1) {
                patternBuilder.append("|");
            }
        }

        patternBuilder.append(")");

        return patternBuilder.toString();
    }

    @Getter
    @ToString
    public static class ResponseWeatherDto {
        private Response response;

        @Getter
        @ToString
        private static class Response {

            private Body body;

            @Getter
            @ToString
            private static class Body {

                private Items items;

                @Getter
                @ToString
                private static class Items {

                    private Item[] item;

                    @Getter
                    @ToString
                    private static class Item {
                        private String baseDate;
                        private String baseTime;
                        private String category;
                        private String fcstDate;
                        private String fcstTime;
                        private String fcstValue;
                        private int nx;
                        private int ny;
                    }
                }
            }
        }
    }
}
