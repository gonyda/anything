package com.bbsk.anything.crawler.investing.controller;

import com.bbsk.anything.crawler.investing.entity.InvestingPerformance;
import com.bbsk.anything.crawler.investing.service.InvestingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/investing")
@Slf4j
public class InventingController {

    private final InvestingService investingService;

    @GetMapping("/performance/{ticker}")
    public ResponseEntity<List<InvestingPerformance>> getPerformance(@PathVariable String ticker) {
        log.info("## ticker: {}", ticker);
        return ResponseEntity.status(HttpStatus.OK).body(investingService.getPerformance(ticker));
    }
}
