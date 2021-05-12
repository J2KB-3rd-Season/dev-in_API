package com.devin.dev.sample;

import java.util.List;
import java.util.Optional;

// 사용자 정의 쿼리 메서드 인터페이스.
// 여기서 선언하고 구현체에 Impl 을 붙여 구현한다.
public interface HelloRepositoryQuery {

    List<Hello> findAllCustom();

    Optional<Hello> findByIdCustom(Long id);

    long countCustom();
}
