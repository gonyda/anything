package com.bbsk.anything.javis.chatGpt.functioncall.dto;

import com.bbsk.anything.javis.chatGpt.functioncall.dto.weather.Functions;
import com.bbsk.anything.javis.utils.JavisModelHolder;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
public class RequestFunctionCall {

    private final String model = JavisModelHolder.get();
    private List<Object> messages;
    private List<Functions> functions;

}
