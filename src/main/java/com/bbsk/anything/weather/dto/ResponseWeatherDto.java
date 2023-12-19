package com.bbsk.anything.weather.dto;

import com.bbsk.anything.weather.constant.BaseDate;
import lombok.Getter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public class ResponseWeatherDto {

    private Response response;

    /**
     * 필요한 데이터만 추출
     * @param categories
     * @param fcstDate
     * @return
     */
    public ResponseWeatherDto filterItemsByCategories(String[] categories, BaseDate fcstDate) {
        List<Response.Body.Items.Item> filteredItems = Arrays.stream(response.body.items.item)
                .filter(item -> Arrays.asList(categories).contains(item.category))
                .filter(item -> item.getFcstDate().equals(fcstDate.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
                .toList();

        response.body.items.item = filteredItems.toArray(new Response.Body.Items.Item[0]);

        return this;
    }

    @Getter
    @ToString
    public static class Response {

        private Body body;

        @Getter
        @ToString
        public static class Body {

            private Items items;

            @Getter
            @ToString
            public static class Items {

                private Item[] item;

                @Getter
                @ToString
                public static class Item {
                    private String baseDate;
                    private String baseTime;
                    private String category;
                    private String fcstDate;
                    private String fcstTime;
                    private String fcstValue;
                    private int nx;
                    private int ny;
                }
            }
        }
    }
}
