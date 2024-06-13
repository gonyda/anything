package com.bbsk.anything.javis.controller;

import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.service.JavisService;
import com.bbsk.anything.javis.service.JavisService.ResponseGptChat;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JavisController {

    private final JavisService javisService;

    @PostMapping("/javis")
    public ResponseEntity<ResponseGptChat> chat(@AuthenticationPrincipal User user, @RequestBody RequestChatByUser dto) {

        return ResponseEntity.status(HttpStatus.OK).body(javisService.callGptApi(dto.updateUser(user)));
    }

    // TODO 새 채팅방, 토큰 초기화
}
