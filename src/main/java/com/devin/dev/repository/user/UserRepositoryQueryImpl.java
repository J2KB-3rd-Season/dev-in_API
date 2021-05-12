package com.devin.dev.repository.user;

import com.devin.dev.entity.user.QUser;
import com.devin.dev.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findUsers() {
        return queryFactory
                .selectFrom(QUser.user)
                .fetch();
    }
}
