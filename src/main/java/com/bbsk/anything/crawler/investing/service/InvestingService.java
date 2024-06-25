package com.bbsk.anything.crawler.investing.service;

import com.bbsk.anything.crawler.investing.constant.InvestingPerformanceEnum;
import com.bbsk.anything.crawler.investing.constant.InvestingPerformanceFactoryEnum;
import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.investing.repository.InvestingPerformanceRepository;
import com.bbsk.anything.crawler.utils.SeleniumUtils;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InvestingService {

    private final InvestingPerformanceRepository investingPerformanceRepository;

    /*
    * 실적 조회
    * */
    @Transactional
    public void getPerformance(String ticker) {
        InvestingPerformanceEnum company = InvestingPerformanceEnum.getByTicker(ticker.toUpperCase());

        WebDriver chromeDriver = SeleniumUtils.getChromeDriver();
        WebElement parentElement = SeleniumUtils.getParentElement(company.getUrl(), company.getFirstClassName(), chromeDriver);
        List<WebElement> childElements = SeleniumUtils.getChildElementsByName(parentElement, company.getSecondClassName());

        // 크롤링 데이터
        List<InvestingPerformance> dataList = getDataList(childElements, company);

        // 기존 데이터
        // 있으면 UPDATE
        // 없으면 INSERT
        saveOrUpdate(dataList);

        SeleniumUtils.close(chromeDriver);
    }

    private List<InvestingPerformance> getDataList(List<WebElement> childElements, InvestingPerformanceEnum company) {
        List<InvestingPerformance> dataList = new ArrayList<>();
        for (WebElement childElement : childElements) {
            dataList.add(
                    InvestingPerformanceFactoryEnum.ENTITY.get(childElement.findElements(By.tagName("td")), company.getName(), company.getTicker())
            );
        }
        // 역순 정렬
        Collections.reverse(dataList);
        return dataList;
    }

    private void saveOrUpdate(List<InvestingPerformance> dataList) {
        dataList.forEach(entity -> {
            InvestingPerformance findEntity = investingPerformanceRepository.findByCompanyAndPeriodEnd(entity.getCompany(), entity.getPeriodEnd());
            if(findEntity == null) {
                investingPerformanceRepository.save(entity);
            } else {
                findEntity.updateValue(entity);
            }
        });
    }
}
