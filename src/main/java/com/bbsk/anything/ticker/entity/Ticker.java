package com.bbsk.anything.ticker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tickerId;

    @Column(nullable = false)
    private String tickerName; // 티커명

    @Column(nullable = false)
    private String company;

    @Column
    private String sector; // 섹터

    @Column
    private String industry; // 산업

}
