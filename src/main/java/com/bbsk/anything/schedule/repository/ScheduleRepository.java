package com.bbsk.anything.schedule.repository;

import com.bbsk.anything.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByUserUserIdOrderByDateAsc(String userId);
}
