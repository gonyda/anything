package com.bbsk.anything;

import com.bbsk.anything.user.dto.RequestUserDto;
import com.bbsk.anything.user.service.UserService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {

        return "home/home";
    }

    @GetMapping("/login")
    public String login() {

        return "home/login";
    }

    @GetMapping("/join")
    public String join() {

        return "home/join";
    }

    @PostMapping("/join-process")
    public String joinProcess(RequestUserDto user) {

        userService.save(user);

        return "redirect:/login";
    }
}
