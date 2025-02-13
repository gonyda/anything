package com.bbsk.anything.user.repository;

import com.bbsk.anything.user.entity.QUser;
import com.bbsk.anything.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.bbsk.anything.naver.news.entity.QNewsKeyword.newsKeyword;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public User findUserAndNewsKeyword(String userId) {
        return queryFactory
                .selectFrom(QUser.user)
                .join(QUser.user.newsKeyword, newsKeyword)
                .fetchJoin()
                .where(QUser.user.userId.eq(userId))
                .fetchOne();
    }
}
