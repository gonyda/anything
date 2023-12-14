package com.bbsk.anything.javis.controller;

import com.bbsk.anything.javis.dto.RequestChatByUser;
import com.bbsk.anything.javis.service.JavisService;
import com.bbsk.anything.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JavisController {

    private final JavisService javisService;

    @PostMapping("/javis")
    public String chat(@AuthenticationPrincipal User user, @RequestBody RequestChatByUser dto) {

        javisService.save(dto.updateUser(user));

        return "";
    }
}
