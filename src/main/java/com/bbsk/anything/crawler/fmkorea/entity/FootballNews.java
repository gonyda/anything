package com.bbsk.anything.crawler.fmkorea.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FootballNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long footballNewsId;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String title;

    @Column(updatable = false)
    private LocalDateTime regDt;

    @PrePersist
    protected void onCreate() {
        regDt = LocalDateTime.now();
    }
}
