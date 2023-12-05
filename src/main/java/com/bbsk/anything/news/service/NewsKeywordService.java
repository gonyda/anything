package com.bbsk.anything.news.service;

import com.bbsk.anything.news.entity.NewsKeyword;
import com.bbsk.anything.news.repository.NewsKeywordRepository;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            /* TODO 네이버 API 사용 */

            return new ResponseSearchNewsDto(StringUtils.join("@", keyword));

        } else {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("회원이 조회되지 않습니다."));

            if (user.getNewsKeyword() == null) {
                return new ResponseSearchNewsDto("");
            } else {
                user.getNewsKeyword().updateSearchCount();
                return new ResponseSearchNewsDto(StringUtils.join("@", user.getNewsKeyword().getKeyword()));
            }
        }
    }

    @Getter
    public class ResponseSearchNewsDto {

        private String keyword;
        /* TODO List 반환 필요, 네이버 뉴스 리스트 */

        public ResponseSearchNewsDto(String keyword) {
            this.keyword = keyword;
        }
    }
}
