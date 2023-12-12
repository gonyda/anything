package com.bbsk.anything.news.service;

import com.bbsk.anything.news.constant.NaverAPI;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class NaverNewsApiService {

    /**
     * 네이버 뉴스 가져오기
     * @param keyword
     * @return
     */
    public News getNews(String keyword) {
        try {
            return ObjectMapperHolder.INSTANCE.get()
                    .readValue(naverNewsApiConnect(keyword).getBody(), News.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 네이버 뉴스 API 호출
     * @param keyword
     * @return
     */
    private ResponseEntity<String> naverNewsApiConnect(String keyword) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(keyword);
        String query = StandardCharsets.UTF_8.decode(buffer).toString();

        URI uri = UriComponentsBuilder.fromUriString (NaverAPI.URI.getValue())
                .path(NaverAPI.PATH.getValue())
                .queryParam("query", query)
                .queryParam("sort", "date")
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> request = RequestEntity
                .get(uri) // http method (get, post, ...)
                .header("X-Naver-Client-Id", NaverAPI.CLIENT_ID.getValue())
                .header("X-Naver-Client-Secret", NaverAPI.CLIENT_SECRET.getValue())
                .build();

        return new RestTemplate().exchange(request, String.class);
    }

    /**
     * 네이버 뉴스 dto
     */
    @Getter
    @ToString
    public static class News {

        private LocalDateTime lastBuildDate;
        private int start; // 시작
        private int display; // 조회 갯수
        private List<NewsItem> items; // 네이버 뉴스 아이템 list

        public void setLastBuildDate(String lastBuildDate) {
            this.lastBuildDate = jasonToLocalDateTime(lastBuildDate);
        }

        private static LocalDateTime jasonToLocalDateTime(String lastBuildDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return LocalDateTime.parse(lastBuildDate, formatter);
        }

        /**
         * 네이버 뉴스 아이템 dto
         */
        @ToString
        @Getter
        public static class NewsItem {

            private String title;
            private String description;
            private String link;
            private LocalDateTime pubDate;

            public void setPubDate(String pubDate) {
                this.pubDate = jasonToLocalDateTime(pubDate);
            }
        }
    }
}
