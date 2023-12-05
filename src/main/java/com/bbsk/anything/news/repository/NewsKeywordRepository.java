package com.bbsk.anything.news.repository;

import com.bbsk.anything.news.entity.NewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsKeywordRepository extends JpaRepository<NewsKeyword, Long> {
    NewsKeyword findByKeyword(String keyword);
}
