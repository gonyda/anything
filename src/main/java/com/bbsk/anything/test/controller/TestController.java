package com.bbsk.anything.test.controller;

import com.bbsk.anything.crawler.utils.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {

    @GetMapping("/selenium-test")
    public void selenium() {
        String url = "https://sports.news.naver.com/wfootball/news/index?isphoto=N";
        String parentClassName = "news_list";
        String childClassName = "title";

        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        WebElement parentElement = SeleniumUtils.getParentElement(url, parentClassName, chromeDriver);
        List<WebElement> childElements = SeleniumUtils.getChildElements(parentElement, childClassName);
        for (WebElement e : childElements) {
            System.out.println("e.getText() = " + e.getText());
            System.out.println("e.getAttribute() = " + e.getAttribute("href"));
        }
        SeleniumUtils.close(chromeDriver);
    }

    @GetMapping("/test")
    public String test() {

        return "test";
    }
}
