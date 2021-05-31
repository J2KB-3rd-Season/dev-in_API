package com.devin.dev.repository.user;

import com.devin.dev.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {

    Optional<User> findByName(String name);

    Optional<User> findByEmailEquals(String email);

    Optional<User> findByEmailEqualsAndPassword(String email, String password);

}
