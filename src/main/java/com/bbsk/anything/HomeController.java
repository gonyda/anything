package com.bbsk.anything;

import com.bbsk.anything.news.service.NewsService;
import com.bbsk.anything.news.service.NewsService.ResponseSearchNewsDto;
import com.bbsk.anything.user.dto.RequestUserDto;
import com.bbsk.anything.user.entity.User;
import com.bbsk.anything.user.service.UserService;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final NewsService newsService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {

        // 회원
        if (user != null) {
            ResponseSearchNewsDto dto = newsService.searchNews(null, user.getUserId());
            model.addAttribute("keyword", dto.getKeyword());
            model.addAttribute("news", dto.getNews());
        }

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
