package com.bbsk.anything.javis.weather.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum Region {

    서울("37", "127", "서울"),
    부산("35", "129", "부산"),
    인천("37", "126", "인천"),
    대구("35", "128", "대구"),
    광주("35", "126", "광주"),
    대전("36", "127", "대전"),
    울산("35", "129", "울산"),
    세종("36", "127", "세종"),
    경기("37", "127", "경기"),
    강원("37", "128", "강원"),
    충북("36", "127", "충북"),
    충남("36", "126", "충남"),
    전북("35", "127", "전북"),
    전남("34", "126", "전남"),
    경북("36", "128", "경북"),
    경남("35", "128", "경남"),
    제주("33", "126", "제주");

    private String x;
    private String y;
    private String city;

}
