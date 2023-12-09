package com.bbsk.anything.news.service;

import com.bbsk.anything.news.constant.NaverAPI;
import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsKeywordService {

    private final NewsKeywordRepository newsKeywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseSearchNewsDto searchNews(String keyword, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 조회되지 않습니다."));

        // 파라미터가 null 아니라면
        if (!StringUtils.isEmpty(keyword)) {
            NewsKeyword newsKeyword = newsKeywordRepository.findByKeyword(keyword);

            // keyword insert
            // OR
            // keyword update count +1
            newsKeyword = newsKeywordRepository.save(
                    newsKeyword == null ? new NewsKeyword().initKeyword(keyword) :
                                          newsKeyword.updateSearchCount()
            );

            // 유저 keyword 세팅
            user.updateKeyword(newsKeyword);

            return new ResponseSearchNewsDto(keyword, getNews(keyword));

        } else {
            // 유저가 keyword를 가지고 있는 지
            if (user.getNewsKeyword() == null) {
                return new ResponseSearchNewsDto("", null);
            } else {
                // keyword count +1
                user.getNewsKeyword().updateSearchCount();

                String userKeyword = user.getNewsKeyword().getKeyword();
                return new ResponseSearchNewsDto(userKeyword, getNews(userKeyword));
            }
        }
    }

    /**
     * 네이버 검색API, 뉴스
     * @param keyword
     * @return
     */
    private News getNews(String keyword) {
        ObjectMapper objectMapper = ObjectMapperHolder.INSTANCE.get();
        try {
            return objectMapper.readValue(naverNewsApiConnect(keyword).getBody(), News.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 네이버 뉴스api 요청
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

        RequestEntity<Void> req = RequestEntity
                .get(uri) // http method (get, post, ...)
                .header("X-Naver-Client-Id", NaverAPI.CLIENT_ID.getValue())
                .header("X-Naver-Client-Secret", NaverAPI.CLIENT_SECRET.getValue())
                .build();

        return new RestTemplate().exchange(req, String.class);
    }

    /**
     * 응답 dto
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public class ResponseSearchNewsDto {
        private String keyword;
        private News news;
    }

    /**
     * 네이버 뉴스
     */
    @Getter
    @ToString
    @NoArgsConstructor
    public static class News {

        private LocalDateTime lastBuildDate;
        private int start; // 시작
        private int display; // 조회 갯수
        private List<NewsItem> items;

        public void setLastBuildDate(String lastBuildDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            this.lastBuildDate = LocalDateTime.parse(lastBuildDate, formatter);
        }

        /**
         * 네이버 뉴스 리스트
         */
        @ToString
        @Getter
        @NoArgsConstructor
        public static class NewsItem {

            private String title;
            private String description;
            private String link;
            private LocalDateTime pubDate;

            public void setPubDate(String pubDate) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                this.pubDate = LocalDateTime.parse(pubDate, formatter);
            }
        }
    }
}
