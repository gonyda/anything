package com.bbsk.anything.naver.news.controller;

import com.bbsk.anything.naver.news.service.NewsService;
import com.bbsk.anything.naver.news.service.NewsService.ResponseSearchNewsDto;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{keyword}")
    public ResponseEntity<ResponseSearchNewsDto> getNews(@PathVariable String keyword, @AuthenticationPrincipal User user) {

        return ResponseEntity.status(HttpStatus.OK).body(newsService.searchNews(keyword, user.getUserId()));
    }
}
