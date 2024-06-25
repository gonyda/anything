package com.bbsk.anything.crawler.investing.service;

import com.bbsk.anything.crawler.investing.constant.InvestingPerformanceEnum;
import com.bbsk.anything.crawler.investing.constant.InvestingPerformanceFactoryEnum;
import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.investing.entity.InvestingPerformance.InvestingPerformanceBuilder;
import com.bbsk.anything.crawler.investing.repository.InvestingPerformanceRepository;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvestingService {

    private final InvestingPerformanceRepository investingPerformanceRepository;

    @Transactional
    public void getPerformance(String ticker) {
        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();

        InvestingPerformanceEnum company = InvestingPerformanceEnum.valueOf(ticker.toUpperCase());

        WebElement parentElement = SeleniumUtils.getParentElement(company.getUrl(), company.getFirstClassName(), chromeDriver);
        List<WebElement> childElements = SeleniumUtils.getChildElementsByName(parentElement, company.getSecondClassName());
        for (WebElement childElement : childElements) {
            int col = 1;
            InvestingPerformanceBuilder builder = InvestingPerformance.builder();
            for (WebElement webElement : childElement.findElements(By.tagName("td"))) {
                InvestingPerformanceFactoryEnum.BUILDER.build(builder, col, webElement.getText());
                builder.company(company.getName());
                col++;
            }

            InvestingPerformance entity = builder.build();
            InvestingPerformance findEntity = investingPerformanceRepository.findByCompanyAndRelDate(entity.getCompany(), entity.getRelDate());
            if(findEntity == null) {
                investingPerformanceRepository.save(entity);
            } else {
                findEntity.updateValue(entity);
            }
        }

        SeleniumUtils.close(chromeDriver);
    }
}
