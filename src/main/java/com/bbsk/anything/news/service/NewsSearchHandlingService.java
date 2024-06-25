package com.bbsk.anything.news.service;

import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.news.service.NewsService.ResponseSearchNewsDto;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsSearchHandlingService {

    private final NaverNewsApiService naverNewsApiService;
    private final NewsKeywordRepository newsKeywordRepository;

    public ResponseSearchNewsDto handleEmptyKeyword(User user) {
        // 유저가 keyword를 가지고 있는 지
        if (user.getNewsKeyword() == null) {
            return new ResponseSearchNewsDto("", null);
        } else {
            // keyword count +1
            user.getNewsKeyword().updateSearchCount();
            return new ResponseSearchNewsDto(user.getNewsKeyword().getKeyword(),
                                                naverNewsApiService.getNews(user.getNewsKeyword().getKeyword()));
        }
    }

    public ResponseSearchNewsDto handleNonEmptyKeyword(String keyword, User user) {
        keyword = keyword.replaceAll("\\s+", "");
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
}
