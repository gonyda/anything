package com.bbsk.anything.crawler.investing.constant;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance.InvestingPerformanceBuilder;

public enum InvestingPerformanceFactoryEnum {

    BUILDER {
        @Override
        public void build(InvestingPerformanceBuilder builder, int col, String value) {
            if(col == 1) {
                builder.relDate(value);
            } else if (col == 2) {
                builder.periodEnd(value);
            } else if (col == 3) {
                builder.eps(value);
            } else if (col == 4) {
                builder.epsForecast(value);
            } else if (col == 5) {
                builder.revenue(value);
            } else if (col == 6) {
                builder.revenueForecast(value);
            }
        }
    };

    public abstract void build(InvestingPerformanceBuilder builder, int col, String value);
}
