package com.bbsk.anything.crawler.yahoo.service;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class YahooFinService {

    // TODO 기업실적 조회 API -> 야후 파이낸셜로 변경
    public void getPerformanceFromYahoo() {
        String url = "https://finance.yahoo.com/quote/AAPL/financials/";
        String firstClassNAme = "table";
        String secondClassNAme = "column";


        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        SeleniumUtils.setChromeDriver(url, chromeDriver);

        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));

        // 버튼이 클릭 가능 상태가 될 때까지 대기
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("tab-quarterly")));
        WebElement button1 = chromeDriver.findElement(By.id("tab-annual"));
        System.out.println("분기 = " + button.getAttribute("aria-selected"));
        System.out.println("연단위 = " + button1.getAttribute("aria-selected"));
        button.click();
        System.out.println("분기 = " + button.getAttribute("aria-selected"));
        System.out.println("연단위 = " + button1.getAttribute("aria-selected"));




        WebElement parentElement = SeleniumUtils.getParentElement(url, firstClassNAme, chromeDriver);
        List<WebElement> childElements = SeleniumUtils.getChildElements(parentElement, secondClassNAme);
        childElements.forEach(e -> {
            log.error(e.getText());
        });
    }
}
