package com.bbsk.anything.news.service;

import com.bbsk.anything.news.constant.NaverAPI;
import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class NewsService {

    private final NewsKeywordRepository newsKeywordRepository;
    private final UserRepository userRepository;

    /**
     * 뉴스 검색
     * @param keyword
     * @param userId
     * @return
     */
    @Transactional
    public ResponseSearchNewsDto searchNews(String keyword, String userId) {
        User user = userRepository.findById(userId).orElseThrow(IllegalAccessError::new);

        if (StringUtils.isEmpty(keyword)) {
            // 유저가 keyword를 가지고 있는 지
            if (user.getNewsKeyword() == null) {
                return new ResponseSearchNewsDto("", null);
            } else {
                // keyword count +1
                user.getNewsKeyword().updateSearchCount();
            }
        } else {
            NewsKeyword findKeyword = newsKeywordRepository.findByKeyword(keyword);

            // 유저 keyword 세팅
            user.updateKeyword(
                    findKeyword == null ?
                    newsKeywordRepository.save(new NewsKeyword().initKeyword(keyword)) : // keyword insert
                    findKeyword.updateSearchCount() // keyword count +1
            );
        }

        String userKeyword = user.getNewsKeyword().getKeyword();
        return new ResponseSearchNewsDto(userKeyword, getNews(userKeyword));
    }

    /**
     * 네이버 뉴스 가져오기
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
     * 응답 dto
     */
    @Getter
    @ToString
    @AllArgsConstructor
    public class ResponseSearchNewsDto {
        private String keyword;
        private News news;
    }

    /**
     * 네이버 뉴스
     */
    @Getter
    @ToString
    public static class News {

        private LocalDateTime lastBuildDate;
        private int start; // 시작
        private int display; // 조회 갯수
        private List<NewsItem> items;

        public void setLastBuildDate(String lastBuildDate) {
            this.lastBuildDate = jasonToLocalDateTime(lastBuildDate);
        }

        /**
         * 네이버 뉴스 리스트
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

        private static LocalDateTime jasonToLocalDateTime(String lastBuildDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return LocalDateTime.parse(lastBuildDate, formatter);
        }
    }
}
