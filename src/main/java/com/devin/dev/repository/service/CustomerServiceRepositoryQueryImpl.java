package com.devin.dev.repository.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerServiceRepositoryQueryImpl implements CustomerServiceRepositoryQuery {
    private final JPAQueryFactory queryFactory;
}
