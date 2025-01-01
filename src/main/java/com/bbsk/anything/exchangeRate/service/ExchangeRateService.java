package com.bbsk.anything.exchangeRate.service;

import com.bbsk.anything.exchangeRate.entity.ExchangeRate;
import com.bbsk.anything.exchangeRate.repository.ExchangeRateRepository;
import com.bbsk.anything.exchangeRate.scheduler.ExchangeRateScheduler.ExchangeRateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.bbsk.anything.exchangeRate.scheduler.ExchangeRateScheduler.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

    /*
    * V1 - 미사용
    * */
    private final static List<String> PARAMS = Arrays.asList("FRX.KRWUSD", "FRX.KRWJPY", "FRX.KRWEUR");

    /*
    * V2 - 사용중
    * */
    private final static List<String> PARAMS_V2 = Arrays.asList("FRX.KRWUSD", "FRX.KRWJPY");

    private final ExchangeRateRepository exchangeRateRepository;

    /*
    * V1 - 미사용
    * */
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
        return exchangeRateRepository.findLatestExchangeRatesByCodes(PARAMS_V2);
    }

    /*
     * V2 - 사용
     * */
    @Transactional
    public void addExchangeInfoV2(ExchangeRateResponseV2Dto exchangeRateResponseV2Dto) {
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .code(isDollar(exchangeRateResponseV2Dto) ? "FRX.KRWUSD" : "FRX.KRWJPY")
                .currencyCode(isDollar(exchangeRateResponseV2Dto) ? "USD" : "JPY")
                .currencyName(exchangeRateResponseV2Dto.getCountry().get(0).getCurrencyUnit())
                .name(isDollar(exchangeRateResponseV2Dto) ? "미국 (USD/KRW)" : "일본 (JPY/KRW)")
                .basePrice(exchangeRateResponseV2Dto.getCountry().get(1).getValue())
                .date(getDate("yyyy-MM-dd", now))
                .time(getTime("HH:mm:ss", now))
                .build();

        exchangeRateRepository.save(exchangeRate);
    }

    private static String getTime(String pattern, LocalDateTime now) {
        DateTimeFormatter HHmmss = DateTimeFormatter.ofPattern(pattern);
        return now.format(HHmmss);
    }

    private static String getDate(String pattern, LocalDateTime now) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern(pattern);
        return now.format(yyyyMMdd);
    }

    private boolean isDollar(ExchangeRateResponseV2Dto exchangeRateResponseV2Dto) {
        return StringUtils.equals(exchangeRateResponseV2Dto.getCountry().get(0).getCurrencyUnit(), "달러");
    }
}
