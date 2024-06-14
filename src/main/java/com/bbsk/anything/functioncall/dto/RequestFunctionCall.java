package com.bbsk.anything.functioncall.dto;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.functioncall.dto.weather.Functions;
import com.bbsk.anything.utils.JavisModelHolder;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
public class RequestFunctionCall {

    private final String model = JavisModelHolder.JAVAIS_MODEL.get();
    private List<Object> messages;
    private List<Functions> functions;

}
