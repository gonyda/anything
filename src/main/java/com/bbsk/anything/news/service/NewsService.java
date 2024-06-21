package com.bbsk.anything.news.service;

import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bbsk.anything.news.service.NaverNewsApiService.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewsService {

    private final NewsKeywordRepository newsKeywordRepository;
    private final UserRepository userRepository;
    private final NewsSearchHandlingService newsSearchHandlingService;

    /**
     * 네이버 뉴스 검색
     * @param keyword
     * @param userId
     * @return
     */
    @Transactional
    public ResponseSearchNewsDto searchNews(String keyword, String userId) {
        User user = userRepository.findById(userId).orElseThrow(IllegalAccessError::new);
        return StringUtils.isEmpty(keyword) ?
                newsSearchHandlingService.handleEmptyKeyword(user) :
                newsSearchHandlingService.handleNonEmptyKeyword(keyword, user);
    }

    /*
     * 검색수 상위 5개 조회
     * */
    public List<NewsKeyword> findTop5ByOrderBySearchCountDesc() {
        return newsKeywordRepository.findTop5ByOrderBySearchCountDesc();
    }

    /**
     * 응답 dto
     */
    @Getter
    @ToString
    @AllArgsConstructor
    public static class ResponseSearchNewsDto {
        private String keyword;
        private News news;
    }
}
