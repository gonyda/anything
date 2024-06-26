package com.bbsk.anything.javis.chatGpt.functioncall.dto.weather;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequestWeatherInfo {
    private String role;
    private String name;
    private String content;
}
