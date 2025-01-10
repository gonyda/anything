package com.bbsk.anything.crawler.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;

public class SeleniumUtils {

    public static WebDriver getChromeDriver() {
        WebDriver driver = new ChromeDriver(ChromeOptionsHolder.INSTANCE.get());
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        Assert.notNull(driver, "## chromeDriver IS NULL");
        return driver;
    }

    public static WebElement getParentElement(String url, String parentClassName, WebDriver chromeDriver) {
        if(isNull(chromeDriver)) {
            setChromeDriver(url, chromeDriver);
        }
        List<WebElement> parentElements = chromeDriver.findElements(By.className(parentClassName));
        Assert.notEmpty(parentElements, "## parentElement IS NULL");
        return parentElements.get(0);
    }

    public static List<WebElement> getChildElements(WebElement parentElement, String childClassName) {
        List<WebElement> childElements = parentElement.findElements(By.className(childClassName));
        Assert.notEmpty(childElements, "## childElements IS EMPTY");
        return childElements;
    }

    public static List<WebElement> getChildElementsByName(WebElement parentElement, String childName) {
        List<WebElement> childElements = parentElement.findElements(By.name(childName));
        Assert.notEmpty(childElements, "## childElements IS EMPTY");
        return childElements;
    }

    public static void close(WebDriver chromeDriver) {
        chromeDriver.close(); // 현재 창 닫기
        chromeDriver.quit(); // 전체 세션 종료
    }

    public static void setChromeDriver(String url, WebDriver chromeDriver) {
        chromeDriver.get(url);
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    private static boolean isNull(WebDriver chromeDriver) {
        return StringUtils.equals(chromeDriver.getCurrentUrl(), "data:");
    }
}
