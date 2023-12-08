package com.bbsk.anything.news.service;

import com.bbsk.anything.news.constant.NaverAPI;
import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsKeywordService {

    private final NewsKeywordRepository newsKeywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseSearchNewsDto searchNews(String keyword, String userId) {
        if (keyword != null) {
            NewsKeyword newsKeyword = newsKeywordRepository.findByKeyword(keyword);

            newsKeyword = newsKeywordRepository.save(
                    newsKeyword == null ? new NewsKeyword().initKeyword(keyword) :
                            newsKeyword.updateSearchCount()
            );

            userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 조회되지 않습니다."))
                    .updateKeyword(newsKeyword);

            return new ResponseSearchNewsDto(StringUtils.join("@", keyword), getNews(keyword));

        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 조회되지 않습니다."));

            if (user.getNewsKeyword() == null) {
                return new ResponseSearchNewsDto("", null);
            } else {
                user.getNewsKeyword().updateSearchCount();
                return new ResponseSearchNewsDto(StringUtils.join("@", user.getNewsKeyword().getKeyword())
                        , getNews(user.getNewsKeyword().getKeyword()));
            }
        }
    }

    /**
     * 네이버 검색API, 뉴스
     * @param keyword
     * @return
     */
    private NewsDto getNews(String keyword) {
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
                .get(uri)
                .header("X-Naver-Client-Id", NaverAPI.CLIENT_ID.getValue())
                .header("X-Naver-Client-Secret", NaverAPI.CLIENT_SECRET.getValue())
                .build();

        ResponseEntity<String> result = new RestTemplate().exchange(req, String.class);

        ObjectMapper ob = new ObjectMapper();
        ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            NewsDto newsDto = ob.registerModule(new JavaTimeModule())
                                .readValue(result.getBody(), NewsDto.class);
            System.out.println("newsDto = " + newsDto.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * News response Dto
     */
    @Getter
    @AllArgsConstructor
    public class ResponseSearchNewsDto {

        private String keyword;
        private NewsDto news;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    public static class NewsDto {

        /*
        * TODO LocalDateTime 구현
        * */
        /*@JsonFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss Z")
        private LocalDateTime lastBuildDate;*/
        private List<NewsItem> items;

        @ToString
        @Getter
        @NoArgsConstructor
        public static class NewsItem {
            private String title;
            private String description;
            private String link;
            /*private String pubDate;*/
        }
    }
}
