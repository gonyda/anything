package com.bbsk.anything.crawler.fmkorea.scheduler;

import com.bbsk.anything.crawler.fmkorea.entity.FootballNews;
import com.bbsk.anything.crawler.fmkorea.service.FmKoreaService;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* 에펨 코리아 - 해외축구 뉴스 소식통
* */
@Component
@RequiredArgsConstructor
@Slf4j
public class FmKoreaScheduler {

    private final FmKoreaService fmKoreaService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void getFmKoreaByFootballNews() {
        log.info("## football news INSERT START");
        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();

        // 크롤링 뉴스 리스트
        List<FootballNews> toSaveList = new ArrayList<>();
        for (WebElement e : fmKoreaService.getFmKoreaFootballNews(chromeDriver)) {
            List<WebElement> webElements = e.findElements(By.tagName("a"));
            for (WebElement ee : webElements) {
                if (!ee.getAttribute("href").contains("/818271708") && !ee.getAttribute("href").contains("/1102273356")) {
                    if (!"replyNum".equals(ee.getAttribute("class"))) {
                        toSaveList.add(
                                FootballNews.builder()
                                        .title(ee.getText())
                                        .link(ee.getAttribute("href"))
                                        .build()
                        );
                    }
                }
            }
        }
        // 정렬 역순
        Collections.reverse(toSaveList);
        // 저장되어 있는 뉴스 리스트
        List<FootballNews> savedList = fmKoreaService.findTop20ByOrderByRegDtDesc();
        // 새로운 뉴스 리스트
        List<FootballNews> newNewsList = toSaveList.stream()
                                                .filter(news -> savedList.stream().noneMatch(savedNews -> savedNews.getLink().equals(news.getLink())))
                                                .toList();
        log.error("## new news list size(): {}", newNewsList.size());
        fmKoreaService.addFootballNews(newNewsList);
        SeleniumUtils.close(chromeDriver);
        log.info("## football news INSERT END");
    }


}
