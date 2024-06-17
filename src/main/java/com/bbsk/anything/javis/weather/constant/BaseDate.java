package com.bbsk.anything.javis.weather.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum BaseDate {

    오늘("오늘", LocalDateTime.now()),
    내일("내일", LocalDateTime.now().plusDays(Duration.ofDays(1L).toDays())),
    모레("모레", LocalDateTime.now().plusDays(Duration.ofDays(2L).toDays()));

    private String day;
    private LocalDateTime localDateTime;

}
