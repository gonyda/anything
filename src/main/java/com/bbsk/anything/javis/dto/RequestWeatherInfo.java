package com.bbsk.anything.javis.dto;

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
