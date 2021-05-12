package com.devin.dev.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 사용자 정의 쿼리 인터페이스를 extends 하여 사용한다.
// 여기서는 메서드 이름만으로 만들어지는 쿼리만 선언한다.
// 복잡한 쿼리는 HelloRepositoryQuery 혹은 다른 클래스를 만들어서 구현한다.
public interface HelloRepository extends JpaRepository<Hello, Long>, HelloRepositoryQuery {
}
