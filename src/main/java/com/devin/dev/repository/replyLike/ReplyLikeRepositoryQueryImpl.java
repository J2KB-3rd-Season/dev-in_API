package com.devin.dev.repository.replyLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class ReplyLikeRepositoryQueryImpl implements ReplyLikeRepositoryQuery {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

}
