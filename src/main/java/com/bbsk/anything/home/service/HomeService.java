package com.bbsk.anything.home.service;

import com.bbsk.anything.crawler.fmkorea.service.FmKoreaService;
import com.bbsk.anything.crawler.investing.service.InvestingService;
import com.bbsk.anything.exchangeRate.entity.ExchangeRate;
import com.bbsk.anything.exchangeRate.service.ExchangeRateService;
import com.bbsk.anything.javis.service.JavisService;
import com.bbsk.anything.naver.news.service.NewsService;
import com.bbsk.anything.schedule.service.ScheduleService;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {

    private final NewsService newsService;
    private final ScheduleService scheduleService;
    private final JavisService javisService;
    private final ExchangeRateService exchangeRateService;
    private final FmKoreaService fmKoreaService;
    private final InvestingService investingService;

    public void getChat(User user, Model model) {
        model.addAttribute("chat", javisService.findAllByUser(user.getUserId()));
    }

    public void getSchedule(User user, Model model) {
        List<ScheduleService.ResponseScheduleDto> list = scheduleService.findAllByUserId(user.getUserId());
        model.addAttribute("scheduleList", list.isEmpty() ? null : list);
    }

    public void getNews(User user, Model model) {
        NewsService.ResponseSearchNewsDto dto = newsService.searchNews(null, user.getUserId());
        model.addAttribute("keyword", dto.getKeyword());
        model.addAttribute("news", dto.getNews());
    }

    public void getExchangeRate(Model model) {
        List<ExchangeRate> exchangeRateList = exchangeRateService.getExchangeRate();
        model.addAttribute("exchangeRate", exchangeRateList);
        model.addAttribute("date", !exchangeRateList.isEmpty() ? exchangeRateList.get(0).getDate() : "");
        model.addAttribute("time", !exchangeRateList.isEmpty() ? extractHour(exchangeRateList.get(0).getTime()) : "");
    }

    public void findTop5ByOrderBySearchCountDesc(Model model) {
        model.addAttribute("hotKeywordList", newsService.findTop5ByOrderBySearchCountDesc());
    }

    public void findTop20ByOrderByRegDtDesc(Model model) {
        model.addAttribute("footballNewsList", fmKoreaService.findTop20ByOrderByRegDtDesc());
    }

    private String extractHour(String time) {
        if (time == null || time.length() < 5) {
            throw new IllegalArgumentException("Invalid time format");
        }
        return time.substring(0, 5);
    }

    public void getPerformance(Model model) {
        model.addAttribute("performanceList", investingService.getPerformance());
    }
}
