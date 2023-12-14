package com.bbsk.anything.javis.entity;

import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.bbsk.anything.javis.service.ChatGptApiService.*;

@Entity
@ToString
@Getter
public class Javis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user; // 채팅 소유자 (유저)

    private LocalDateTime createTime; // 메시지 생성시간

    @Column(nullable = false)
    private String sender; // 보내는 사람 (user or javis)

    @Column(nullable = false)
    private String model; // 사용 gpt모델

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; // 메시지

    private Long totalTokens;

    public Javis toEntity(RequestChatByUser dto) {
        this.user = dto.getUser();
        this.createTime = dto.getCreateTime();
        this.sender = dto.getMessages()[dto.getMessages().length -1].getRole();
        this.model = dto.getModel();
        this.message = dto.getMessages()[dto.getMessages().length -1].getContent();

        return this;
    }

    public Javis toEntity(ResponseChatByGpt dto, User user) {
        this.user = user;
        this.createTime = dto.getCreated();
        this.sender = dto.getChoices()[0].getMessage().getRole();
        this.model = dto.getModel();
        this.message = dto.getChoices()[0].getMessage().getContent();
        this.totalTokens = dto.getUsage().getTotal_tokens();

        return this;
    }
}
