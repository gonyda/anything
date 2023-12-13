package com.bbsk.anything.schedule.repository;

import com.bbsk.anything.schedule.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.bbsk.anything.schedule.entity.QSchedule.*;

public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ScheduleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 해당 유저의 일정 가져오기
     * 단, 현재 시간 기준으로 지난 일정은 가져오지 않는다
     * @param userId
     * @return
     */
    @Override
    public List<Schedule> findAllByUserId(String userId) {
        return queryFactory
                .selectFrom(schedule)
                .where(
                        schedule.user.userId.eq(userId),
                        schedule.date.goe(LocalDateTime.now())
                )
                .orderBy(schedule.date.asc())
                .fetch();
    }
}
