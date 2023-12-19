package com.bbsk.anything.javis.dto;

import com.bbsk.anything.javis.constant.ChatGptModel;
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
    private List<Function> functions;

}
