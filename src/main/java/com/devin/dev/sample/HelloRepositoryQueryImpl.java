package com.devin.dev.sample;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.devin.dev.sample.QHello.*;

// 사용자 정의 interface 를 만들고 구현한다.
// 구현체 이름은 ~~~Impl (Impl 안붙으면 에러남)
@RequiredArgsConstructor
public class HelloRepositoryQueryImpl implements HelloRepositoryQuery {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Hello> findAllCustom() {
        return queryFactory
                .selectFrom(hello)
                .fetch();
    }

    @Override
    public Optional<Hello> findByIdCustom(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(hello) // QHello 클래스 static import
                .where(hello.id.eq(id))
                .fetchOne());
    }

    @Override
    public long countCustom() {
        return queryFactory
                .select(hello.id)
                .from(hello)
                .fetchCount();
    }
}
