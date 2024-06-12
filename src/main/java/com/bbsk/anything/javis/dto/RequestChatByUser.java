package com.bbsk.anything.javis.dto;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.user.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class RequestChatByUser {
    private List<Message> messages;
    private LocalDateTime createTime;
    private User user;
    private final String model = ChatGptModel.GPT_3_5_TURBO_0125.getName();

    public void setCreateTime(String createTime) {
        this.createTime = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public RequestChatByUser updateUser(User user) {
        this.user = user;
        return this;
    }
}
