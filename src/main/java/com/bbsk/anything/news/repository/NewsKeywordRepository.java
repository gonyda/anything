package com.bbsk.anything.news.repository;

import com.bbsk.anything.news.entity.NewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsKeywordRepository extends JpaRepository<NewsKeyword, Long> {
    NewsKeyword findByKeyword(String keyword);

    List<NewsKeyword> findTop5ByOrderBySearchCountDesc();
}
