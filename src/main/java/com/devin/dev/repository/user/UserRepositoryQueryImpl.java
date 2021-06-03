package com.devin.dev.repository.user;

import com.devin.dev.dto.user.QUserDetailsDto;
import com.devin.dev.dto.user.UserDetailsDto;
import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.QUser;
import com.devin.dev.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.devin.dev.entity.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findUsers() {
        return queryFactory
                .selectFrom(user)
                .fetch();
    }

    @Override
    public Optional<UserSimpleDto> findUserDtoById(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDetailsDto> findUserDetailsByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .select(new QUserDetailsDto(
                        user.id,
                        user.name,
                        user.email,
                        user.password,
                        user.phone_number,
                        user.exp,
                        user.profile,
                        user.status,
                        user.sns_type,
                        user.description
                ))
                .from(user)
                .where(user.email.eq(email))
                .fetchOne());
    }
}
