package com.bbsk.anything.javis.service;

import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.dto.ResponseChatByGpt;
import com.bbsk.anything.javis.entity.Javis;
import com.bbsk.anything.javis.repository.JavisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JavisService {

    private final JavisRepository javisRepository;
    private final ChatGptApiService chatGptApiService;

    @Transactional
    public void save(RequestChatByUser dto) {
        // 유저 채팅 저장
        javisRepository.save(new Javis().toEntity(dto));

        // API Connect
        ResponseChatByGpt chat = chatGptApiService.getChat(dto);

        // GPT 채팅 저장
        javisRepository.save(new Javis().toEntity(chat, dto.getUser()));
    }
}
