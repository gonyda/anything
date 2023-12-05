package com.bbsk.anything.news.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class NewsKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;

    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    private Long searchCount;

    /**
     * 처음으로 검색된 키워드
     * @param keyword
     * @return
     */
    public NewsKeyword initKeyword(String keyword) {
        this.keyword = keyword;
        this.searchCount = 1L;
        return this;
    }

    /**
     * 기존에 존재하는 키워드
     * 카운트 + 1
     * @return
     */
    public NewsKeyword updateSearchCount() {
        this.searchCount++;
        return this;
    }
}
