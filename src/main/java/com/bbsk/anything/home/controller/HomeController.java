package com.bbsk.anything.home.controller;

import com.bbsk.anything.exchangeRate.entity.ExchangeRate;
import com.bbsk.anything.exchangeRate.service.ExchangeRateService;
import com.bbsk.anything.home.service.HomeService;
import com.bbsk.anything.javis.service.JavisService;
import com.bbsk.anything.news.service.NewsService;
import com.bbsk.anything.news.service.NewsService.ResponseSearchNewsDto;
import com.bbsk.anything.schedule.service.ScheduleService;
import com.bbsk.anything.user.dto.RequestUserDto;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.bbsk.anything.schedule.service.ScheduleService.ResponseScheduleDto;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomeService homeService;
    private final UserService userService;


    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {
            // 뉴스 조회
            homeService.getNews(user, model);
            // 일정 조회
            homeService.getSchedule(user, model);
            // 자비스 채팅 조회
            homeService.getChat(user, model);
            // 검색순 상위 5개 조회
            homeService.findTop5ByOrderBySearchCountDesc(model);
            // 펨코 해외축구 뉴스
            // 상위 20개 조회
            homeService.findTop20ByOrderByRegDtDesc(model);
        }
        // 한율 조회
        homeService.getExchangeRate(model);
        // 기업 실적
        homeService.getPerformance(model);

        return "home/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // 한율 조회
        homeService.getExchangeRate(model);
        return "home/login";
    }

    @GetMapping("/join")
    public String join(Model model) {
        // 한율 조회
        homeService.getExchangeRate(model);
        return "home/join";
    }

    @PostMapping("/join-process")
    public String joinProcess(RequestUserDto user) {

        userService.save(user);

        return "redirect:/login";
    }
}
