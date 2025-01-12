package com.bbsk.anything.ticker.service;

import com.bbsk.anything.ticker.entity.Ticker;
import com.bbsk.anything.ticker.repository.TickerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TickerService {

    private final TickerRepository tickerRepository;

    public List<ResponseTickerDto> getTicker(String ticker) {
        // TODO DB 커넥션을 줄이기 위한 캐시처리
        List<Ticker> tickers = tickerRepository.findTop10ByTickerNameContainingOrderByTickerId(ticker.toUpperCase());

        if (tickers.isEmpty()) {
            throw new RuntimeException(" ## ERROR 해당하는 티커를 찾을 수 없습니다: " + ticker);
        }

        return tickers.stream()
                .map(t -> new ResponseTickerDto(
                        t.getTickerName(),
                        t.getCompany(),
                        t.getSector(),
                        t.getIndustry()
                ))
                .toList();
    }

    /**
     * 응답 dto
     */
    @Getter
    @ToString
    @AllArgsConstructor
    public static class ResponseTickerDto {
        public String ticker;
        public String company;
        public String sector;
        public String industry;
    }
}
