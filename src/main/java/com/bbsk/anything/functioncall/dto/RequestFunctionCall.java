package com.bbsk.anything.functioncall.dto;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.functioncall.dto.weather.Functions;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
public class RequestFunctionCall {

    private final String model = ChatGptModel.GPT_3_5_TURBO_1106.getName();
    private List<Object> messages;
    private List<Functions> functions;

}
