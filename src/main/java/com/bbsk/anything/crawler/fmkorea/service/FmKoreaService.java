package com.bbsk.anything.crawler.fmkorea.service;

import com.bbsk.anything.crawler.fmkorea.entity.FootballNews;
import com.bbsk.anything.crawler.fmkorea.repository.FootBallNewsRepository;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FmKoreaService {

    private final FootBallNewsRepository footBallNewsRepository;

    private static final String URL = "https://www.fmkorea.com/football_news";
    private static final String FIRST_CLASSNAME = "bd_lst";
    private static final String SECOND_CLASSNAME = "title";

    public List<WebElement> getFmKoreaFootballNews(){
        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        WebElement parentElement = SeleniumUtils.getParentElement(URL, FIRST_CLASSNAME, chromeDriver);
        return SeleniumUtils.getChildElements(parentElement, SECOND_CLASSNAME);
    }

    @Transactional
    public void addFootballNews(List<FootballNews> footballNews) {
        footBallNewsRepository.saveAll(footballNews);
    }

    public List<FootballNews> findTop22ByOrderByRegDtDesc() {
        return footBallNewsRepository.findTop20ByOrderByRegDtDesc();
    }

    public List<FootballNews> findTop20ByOrderByRegDtDesc() {
        return footBallNewsRepository.findTop20ByOrderByRegDtDesc();
    }
}
