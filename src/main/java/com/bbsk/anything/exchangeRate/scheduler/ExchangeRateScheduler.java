package com.bbsk.anything.exchangeRate.scheduler;

import com.bbsk.anything.exchangeRate.constant.ApiConfig;
import com.bbsk.anything.exchangeRate.service.ExchangeRateService;
import com.bbsk.anything.utils.ObjectMapperHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 0 * * * ?")
    public void getExchangeRate() {
        log.info("## getExchangeRate() START");
        URI uri = UriComponentsBuilder.fromUriString(ApiConfig.API_URL.getValue())
                .path(ApiConfig.API_URL_PATH.getValue())
                .queryParam("codes", ApiConfig.PARAMS.getArrValue())
                .encode()
                .build()
                .toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(request, String.class);
        try {
            List<ExchangeRateResponseDto> exchangeRateResponseDtoList =
                    Arrays.stream(ObjectMapperHolder.INSTANCE.get().readValue(responseEntity.getBody(), ExchangeRateResponseDto[].class))
                    .toList();
            exchangeRateService.addExchangeInfo(exchangeRateResponseDtoList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Getter
    @ToString
    public static class ExchangeRateResponseDto {
        private String code;
        private String currencyCode;
        private String currencyName;
        private String name;
        private String date;
        private String time;
        private String basePrice;
    }
}
