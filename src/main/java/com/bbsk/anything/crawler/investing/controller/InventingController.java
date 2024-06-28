package com.bbsk.anything.crawler.investing.controller;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.investing.service.InvestingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/investing")
@Slf4j
public class InventingController {

    private final InvestingService investingService;

    /*
    * 기존 저장되어있는 실적 데이터 업데이트
    * */
    @PutMapping("/performance/{ticker}")
    public ResponseEntity<List<InvestingPerformance>> modifyPerformance(@PathVariable String ticker) {
        log.info("## ticker: {}", ticker);
        return ResponseEntity.status(HttpStatus.OK).body(investingService.modifyPerformance(ticker));
    }

    /*
    * DB에 저장되어 있는 실적 데이터 조회
    * */
    @GetMapping("/performance")
    public ResponseEntity<List<InvestingService.InvestingPerformanceResponseDto>> getPerformance() {
        return ResponseEntity.status(HttpStatus.OK).body(investingService.getPerformance());
    }
}
