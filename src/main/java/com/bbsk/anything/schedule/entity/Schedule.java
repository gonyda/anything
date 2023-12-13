package com.bbsk.anything.schedule.entity;

import com.bbsk.anything.schedule.controller.ScheduleController;
import com.bbsk.anything.schedule.service.ScheduleService;
import com.bbsk.anything.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static com.bbsk.anything.schedule.controller.ScheduleController.*;
import static com.bbsk.anything.schedule.service.ScheduleService.*;

@Entity
@Getter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;

    public Schedule toEntity(RequestScheduleDto dto) {
        this.user = dto.getUser();
        this.content = dto.getContent();;
        this.date = dto.getDate();
        return this;
    }
}
