package com.bbsk.anything.schedule.repository;

import com.bbsk.anything.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<Schedule> findAllByUserId(String userId);
}
