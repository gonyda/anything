package com.bbsk.anything.javis.weather.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum WeatherApi {
    URL("http://apis.data.go.kr"),
    PATH("/1360000/VilageFcstInfoService_2.0/getVilageFcst"),
    SERVICEKEY("eImJh%2BTQnXmqHHWKs5I6OdVJjfQ0wAdHaxcjgyv%2Fj4h9mu5DvYxTchTFc9Jyty3COOAxnlUETaIHSCEtHu25JQ%3D%3D"),
    DATATYPE("JSON");

    private String value;
}
