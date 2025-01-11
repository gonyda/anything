package com.bbsk.anything.naver.news.repository;

import com.bbsk.anything.naver.news.entity.NewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsKeywordRepository extends JpaRepository<NewsKeyword, Long> {
    NewsKeyword findByKeyword(String keyword);

    List<NewsKeyword> findTop5ByOrderBySearchCountDesc();
}
