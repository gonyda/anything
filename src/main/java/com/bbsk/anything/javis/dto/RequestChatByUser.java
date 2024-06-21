package com.bbsk.anything.javis.dto;

import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.javis.utils.JavisModelHolder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@ToString
public class RequestChatByUser {
    private List<Message> messages;
    private LocalDateTime createTime;
    private User user;
    private final String model = JavisModelHolder.get();

    public void setCreateTime(String createTime) {
        this.createTime = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public RequestChatByUser updateUser(User user) {
        this.user = user;
        return this;
    }
}
