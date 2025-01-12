package com.bbsk.anything.ticker.controller;

import com.bbsk.anything.ticker.service.TickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bbsk.anything.ticker.service.TickerService.*;

@RestController
@RequestMapping("/ticker")
@RequiredArgsConstructor
public class TickerController {

    private final TickerService tickerService;

    @GetMapping("/{ticker}")
    public ResponseEntity<List<ResponseTickerDto>> getTicker(@PathVariable String ticker) {
        return ResponseEntity.status(HttpStatus.OK).body(tickerService.getTicker(ticker));
    }
}
