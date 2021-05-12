package com.devin.dev.sample;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.devin.dev.sample.QHello.hello;

// 기존 방식의 JPA repository

@Repository
public class HelloJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public HelloJpaRepository(EntityManager em, JPAQueryFactory queryFactory) {
        this.em = em;
        this.queryFactory = queryFactory;
    }

    @org.springframework.transaction.annotation.Transactional
    public void save(Hello hello) {
        em.persist(hello);
    }

    public List<Hello> findAll() {
        return queryFactory
                .selectFrom(hello)
                .fetch();
    }

    public Optional<Hello> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .select(hello)
                        .from(hello)
                        .where(hello.id.eq(id))
                        .fetchOne());
    }

    @Transactional
    public void delete(Hello hello) {
        em.remove(hello);
    }

    public long count() {
        return queryFactory
                .selectFrom(hello)
                .fetchCount();
    }
}
