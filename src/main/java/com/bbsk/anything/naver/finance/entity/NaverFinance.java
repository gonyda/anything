package com.bbsk.anything.naver.finance.entity;

import com.bbsk.anything.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class NaverFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") // foreign key 설정
    @ToString.Exclude
    private User user; // 연관된 유저 엔티티

    @Column(nullable = false)
    private String company; // 화사명

    @Column(nullable = false)
    private String ticker;

    @Column
    private String relDate; // 발표일

    @Column
    private String eps; // eps, 주당순이익

    @Column
    private String salesRevenue; // 매출액

    @Column
    private String netIncome; // 당기순이익

    @Column
    private String netProfitMargin; // 순이익마진율

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDt;

    @PrePersist
    public void prePersist() {
        this.createdDt = LocalDateTime.now();
    }
}
