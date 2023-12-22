package com.bbsk.anything.functioncall.dto.weather;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Functions {

    private String name;
    private String description;
    private Parameters parameters;

    public Functions() {
        this.name = "get_current_weather";
        this.description = "주어진 지역의 날씨, 강수량, 하늘상태를 알려줍니다. 최저기온 및 최고기온을 알려주어야 합니다. 또한 예보시간대별로도 알려주어야 합니다.";
        this.parameters = new Parameters();
    }

    @Getter
    @ToString
    public static class Parameters {
        private String type;
        private String[] required;
        private Properties properties;

        public Parameters() {
            this.type = "object";
            this.required = new String[]{"location", "category", "fcstDate", "fcstTime", "fcstValue"};
            this.properties = new Properties();
        }

        @Getter
        @ToString
        public static class Properties {

            private Location location;
            private Category category;
            private FcstDate fcstDate;
            private FcstTime fcstTime;
            private FcstValue fcstValue;

            public Properties() {
                this.location = new Location().init();
                this.category = new Category().init();
                this.fcstDate = new FcstDate().init();
                this.fcstTime = new FcstTime().init();
                this.fcstValue = new FcstValue().init();
            }

            @Getter
            @ToString
            private static class Location {
                private String type;
                private String description;

                public Location init() {
                    this.type = "string";
                    this.description = "도시 (예: 서울, 부산 등등)";
                    return this;
                }
            }

            @Getter
            @ToString
            private static class Category {
                private String type;
                private String description;

                public Category init() {
                    this.type = "string";
                    StringBuilder sb = new StringBuilder();
                    sb.append("날씨 데이터의 종류 입니다. ");
                    sb.append("TMP: 시간별 기온 ");
                    sb.append("POP: 시간별 강수량 ");
                    sb.append("SKY: 하늘상태 ");
                    sb.append("SKY의 값은 맑음(1), 구름많음(3), 흐림(4) 입니다.");
                    this.description = sb.toString();
                    return this;
                }
            }

            @Getter
            @ToString
            private static class FcstDate {
                private String type;
                private String description;

                public FcstDate init() {
                    this.type = "string";
                    this.description = "예보일자 입니다";
                    return this;
                }
            }

            @Getter
            @ToString
            private static class FcstTime {
                private String type;
                private String description;

                public FcstTime init() {
                    this.type = "string";
                    this.description = "예보시간 입니다.";
                    return this;
                }
            }

            @Getter
            @ToString
            private static class FcstValue {
                private String type;
                private String description;

                public FcstValue init() {
                    this.type = "string";
                    StringBuilder sb = new StringBuilder();
                    sb.append("날씨 데이터의 값입니다 ");
                    sb.append("category의 따라 값이 다릅니다. ");
                    sb.append("TMP: 시간별 기온 ");
                    sb.append("POP: 시간별 강수량 ");
                    sb.append("SKY: 하늘상태");
                    this.description = sb.toString();
                    return this;
                }
            }
        }
    }
}
