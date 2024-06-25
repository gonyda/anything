package com.bbsk.anything.crawler.investing.constant;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.investing.entity.InvestingPerformance.InvestingPerformanceBuilder;
import org.openqa.selenium.WebElement;

import java.util.List;

public enum InvestingPerformanceFactoryEnum {

    ENTITY {
        @Override
        public InvestingPerformance get(List<WebElement> webElements, String companyName) {
            int col = 1;
            InvestingPerformanceBuilder builder = InvestingPerformance.builder();
            for (WebElement webElement : webElements) {
                String value = webElement.getText();
                if(col == 1) {
                    builder.relDate(value);
                } else if (col == 2) {
                    builder.periodEnd(value);
                } else if (col == 3) {
                    builder.eps(value);
                } else if (col == 4) {
                    builder.epsForecast(value.substring(3));
                } else if (col == 5) {
                    builder.revenue(value);
                } else if (col == 6) {
                    builder.revenueForecast(value.substring(3));
                }
                col++;
            }
            builder.company(companyName);
            return builder.build();
        }
    };

    public abstract InvestingPerformance get(List<WebElement> webElements, String name);
}
