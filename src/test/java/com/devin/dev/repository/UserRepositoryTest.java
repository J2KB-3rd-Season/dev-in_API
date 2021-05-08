package com.devin.dev.repository;

import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void createOne() {
        User user = new User("A", "a@b.com", "pass", "0000", UserStatus.ACTIVE);

        userRepository.save(user);

        User findUser = userRepository.findById(user.getId()).get();

        assertThat(user).isEqualTo(findUser);
    }
}