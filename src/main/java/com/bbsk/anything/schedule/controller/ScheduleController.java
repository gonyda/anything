package com.bbsk.anything.schedule.controller;

import com.bbsk.anything.schedule.service.ScheduleService;
import com.bbsk.anything.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.bbsk.anything.schedule.service.ScheduleService.*;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedule")
    public ResponseEntity<List<ResponseScheduleDto>> register(@RequestBody RequestScheduleDto dto, @AuthenticationPrincipal User user) {

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.save(dto.setUser(user)));
    }

    @Getter
    @ToString
    public static class RequestScheduleDto {
        private String content;
        private LocalDateTime date;
        private User user;

        public RequestScheduleDto setUser(User user) {
            this.user = user;
            return this;
        }
    }
}
