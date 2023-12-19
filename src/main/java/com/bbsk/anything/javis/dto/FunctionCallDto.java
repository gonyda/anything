package com.bbsk.anything.javis.dto;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.weather.dto.RequestFunctionCallDto;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
public class FunctionCallDto {

    private final String model = ChatGptModel.GPT_3_5_TURBO_1106.getName();
    private List<Message> messages;
    private List<RequestFunctionCallDto> functions;

}
