package com.bbsk.anything.news.service;

import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bbsk.anything.news.service.NaverNewsApiService.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsKeywordRepository newsKeywordRepository;
    private final UserRepository userRepository;
    private final NaverNewsApiService naverNewsApiService;

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
                handleEmptyKeyword(user) :
                handleNonEmptyKeyword(keyword, user);
    }

    private ResponseSearchNewsDto handleEmptyKeyword(User user) {
        // 유저가 keyword를 가지고 있는 지
        if (user.getNewsKeyword() == null) {
            return new ResponseSearchNewsDto("", null);
        } else {
            // keyword count +1
            user.getNewsKeyword().updateSearchCount();
            String userKeyword = user.getNewsKeyword().getKeyword();
            return new ResponseSearchNewsDto(userKeyword, naverNewsApiService.getNews(userKeyword));
        }
    }

    private ResponseSearchNewsDto handleNonEmptyKeyword(String keyword, User user) {
        NewsKeyword findKeyword = newsKeywordRepository.findByKeyword(keyword);

        // 유저 keyword 세팅
        user.updateKeyword(
                findKeyword == null ?
                        newsKeywordRepository.save(new NewsKeyword().initKeyword(keyword)) : // keyword insert
                        findKeyword.updateSearchCount() // keyword count +1
        );
        String userKeyword = user.getNewsKeyword().getKeyword();
        return new ResponseSearchNewsDto(userKeyword, naverNewsApiService.getNews(userKeyword));
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
