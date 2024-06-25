package com.bbsk.anything.crawler.investing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InvestingPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String relDate; // 실적 발표일

    @Column(nullable = false)
    private String periodEnd; // 마감 기준일

    @Column(nullable = false)
    private String eps; // 주당 순이익

    @Column(nullable = false)
    private String epsForecast; // 주당 순이익 예측

    @Column(nullable = false)
    private String revenue; // 매출

    @Column(nullable = false)
    private String revenueForecast; // 매출 예측

    public void updateValue(InvestingPerformance entity) {
        this.eps = entity.getEps();
        this.epsForecast = entity.getEpsForecast();
        this.revenue = entity.getRevenue();
        this.revenueForecast = entity.getRevenueForecast();
    }
}
