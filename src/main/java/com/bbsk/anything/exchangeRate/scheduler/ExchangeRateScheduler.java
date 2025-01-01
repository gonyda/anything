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

    /*
    * @Scheduled(cron = "0 0 * * * ?")
    * 해당 API 서비스 중단
    * */
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

    @Scheduled(cron = "0 0 * * * ?")
    public void getExchangeRateV2() {
        log.info("## getExchangeRateV2() START");
        List<String> queryParams = Arrays.stream(ApiConfig.PARAMS_V2.getArrValue()).toList();
        for (String queryParam : queryParams) {
            URI uri = UriComponentsBuilder.fromUriString(ApiConfig.API_URL_V2.getValue())
                    .path(ApiConfig.API_URL_PATH_V2.getValue())
                    .queryParam("key", "calculator")
                    .queryParam("pkid", "141")
                    .queryParam("q", "환율")
                    .queryParam("where", "m")
                    .queryParam("u1", "keb")
                    .queryParam("u6", "standardUnit")
                    .queryParam("u7", "0")
                    .queryParam("u3", queryParam)
                    .queryParam("u4", "KRW")
                    .queryParam("u8", "down")
                    .queryParam("u2", "1")
                    .encode()
                    .build()
                    .toUri();
            RequestEntity<Void> request = RequestEntity.get(uri).build();
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(request, String.class);

            try {
                ExchangeRateResponseV2Dto exchangeRateResponseV2Dto =
                        ObjectMapperHolder.INSTANCE.get().readValue(responseEntity.getBody(), ExchangeRateResponseV2Dto.class);
                exchangeRateService.addExchangeInfoV2(exchangeRateResponseV2Dto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }



    @Getter
    @ToString
    public static class ExchangeRateResponseDto {
        private String code;
        private String currencyCode;
        private String currencyName;
        private String basePrice;
        private String name;
        private String date;
        private String time;
    }

    @Getter
    @ToString
    public static class ExchangeRateResponseV2Dto {
        private int pkid;
        private int count;
        private List<Country> country;
        private String calculatorMessage;

        @Getter
        @ToString
        public static class Country {
            private String value;
            private String subValue;
            private String currencyUnit;
        }
    }

}
