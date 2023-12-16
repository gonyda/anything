package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.constant.ChatGptModel;
import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.javis.entity.Javis;
import com.bbsk.anything.javis.repository.JavisRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JavisService {

    private final JavisRepository javisRepository;
    private final ChatGptApiService chatGptApiService;

    @Transactional
    public ResponseGptChat callGptApi(RequestChatByUser dto) {
        // 유저 채팅 저장
        javisRepository.save(new Javis().toEntity(dto));

        // API Connect
        ResponseChatByGpt chat = chatGptApiService.getChat(dto);

        // GPT 채팅 저장
        Javis gptChat = javisRepository.save(new Javis().toEntity(chat, dto.getUser()));

        return new ResponseGptChat().toDto(gptChat);
    }

    /**
     * 해당 유저의 채팅 내역 가져져오기
     *
     * @param userId
     * @return
     */
    public List<ResponseGptChat> findAllByUser(String userId) {
        List<ResponseGptChat> list = new ArrayList<>();

        javisRepository.findAllByUserUserIdOrderByChatIdAsc(userId).forEach(e -> {
            list.add(new ResponseGptChat().toDto(e));
        });

        return list;
    }

    @Getter
    @ToString
    public static class ResponseGptChat {
        private String sender;
        private LocalDateTime createTime;
        private String message;
        private Long totalTokens;
        private Long maxTokens;
        private String model;

        public ResponseGptChat toDto(Javis entity) {
            this.sender = entity.getSender();
            this.createTime = entity.getCreateTime();
            this.message = entity.getMessage();
            this.totalTokens = entity.getTotalTokens();
            this.maxTokens = ChatGptModel.GPT_3_5_TURBO_1106.getTokens();
            this.model = entity.getModel();

            return this;
        }
    }
}
