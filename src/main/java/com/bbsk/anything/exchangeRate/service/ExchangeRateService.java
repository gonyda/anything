package com.bbsk.anything.exchangeRate.service;

import com.bbsk.anything.exchangeRate.entity.ExchangeRate;
import com.bbsk.anything.exchangeRate.repository.ExchangeRateRepository;
import com.bbsk.anything.exchangeRate.scheduler.ExchangeRateScheduler.ExchangeRateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExchangeRateService {

    private final static List<String> PARAMS = Arrays.asList("FRX.KRWUSD", "FRX.KRWJPY", "FRX.KRWEUR");

    private final ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public void addExchangeInfo(List<ExchangeRateResponseDto> exchangeRateResponseDtoList) {
        List<ExchangeRate> exchangeRateEntityList = exchangeRateResponseDtoList.stream()
                .map(dto -> ExchangeRate.builder()
                        .code(dto.getCode())
                        .currencyCode(dto.getCurrencyCode())
                        .currencyName(dto.getCurrencyName())
                        .name(dto.getName())
                        .basePrice(dto.getBasePrice())
                        .date(dto.getDate())
                        .time(dto.getTime())
                        .build())
                .toList();

        // TODO bulk insert로 변경
        exchangeRateRepository.saveAll(exchangeRateEntityList);
    }

    public List<ExchangeRate> getExchangeRate() {
        return exchangeRateRepository.findLatestExchangeRatesByCodes(PARAMS);
    }
}
