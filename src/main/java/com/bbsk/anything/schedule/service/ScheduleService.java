package com.bbsk.anything.schedule.service;

import com.bbsk.anything.news.constant.NaverAPI;
import com.bbsk.anything.schedule.entity.Schedule;
import com.bbsk.anything.schedule.repository.ScheduleRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bbsk.anything.schedule.controller.ScheduleController.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<ResponseScheduleDto> findAllByUserId(String userId) {
        List<ResponseScheduleDto> list = new ArrayList<>();

        scheduleRepository.findAllByUserUserIdOrderByDateAsc(userId).stream().forEach(e ->{
            list.add(new ResponseScheduleDto().toDto(e));
        });

        return list;
    }

    @Transactional
    public List<ResponseScheduleDto> save(RequestScheduleDto dto) {

        scheduleRepository.save(new Schedule().toEntity(dto));

        return findAllByUserId(dto.getUser().getUserId());
    }

    @Getter
    @ToString
    public static class ResponseScheduleDto {
        private Long scheduleId;
        private String content;
        private LocalDateTime date;
        private String dayOfWeek;

        public ResponseScheduleDto toDto(Schedule entity) {
            this.scheduleId = entity.getScheduleId();
            this.content = entity.getContent();
            this.date = entity.getDate();
            this.dayOfWeek = getDayOfWeek(entity.getDate().getDayOfWeek());

            return this;
        }

        /**
         * 요일 구하기
         *
         * @param dayOfWeek
         * @return
         */
        private String getDayOfWeek(DayOfWeek dayOfWeek) {
            switch (dayOfWeek) {
                case MONDAY -> {
                    return "월요일";
                }
                case TUESDAY -> {
                    return "화요일";
                }
                case WEDNESDAY -> {
                    return "수요일";
                }
                case THURSDAY -> {
                    return "목요일";
                }
                case FRIDAY -> {
                    return  "금요일";
                }
                case SATURDAY -> {
                    return  "토요일";
                }
                case SUNDAY -> {
                    return  "일요일";
                }
                default -> {
                    throw new RuntimeException("요일을 구할 수 없습니다.");
                }
            }
        }
    }
}
