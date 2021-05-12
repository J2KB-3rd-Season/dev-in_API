package com.devin.dev.repository;

import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void saveOne() {
        User user = new User("A", "a@b.com", "pass", "0000", UserStatus.ACTIVE);

        userRepository.save(user);

        User findUser = em.find(User.class, user.getId());

        assertThat(user).isEqualTo(findUser);
    }

    @Test
    void findOne() {
        User user = new User("A", "a@b.com", "pass", "0000", UserStatus.ACTIVE);
        em.persist(user);

        User findUser = userRepository.findById(user.getId()).get();

        assertThat(user).isEqualTo(findUser);
    }

    @Test
    void findByName() {
        User user = new User("A", "a@b.com", "pass", "0000", UserStatus.ACTIVE);
        em.persist(user);

        User findUser = userRepository.findByName("A").get();

        assertThat(user).isEqualTo(findUser);
    }

    @Test
    void deleteOne() {
        User user = new User("A", "a@b.com", "pass", "0000", UserStatus.ACTIVE);
        em.persist(user);

        userRepository.delete(user);

        assertThat(em.find(User.class, user.getId())).isNull();
    }

    @Test
    void findAll() {
        User userA = new User("A", "a@b.com", "passA", "0001", UserStatus.ACTIVE);
        em.persist(userA);
        User userB = new User("B", "b@b.com", "passB", "0002", UserStatus.DELETED);
        em.persist(userB);
        User userC = new User("C", "c@b.com", "passC", "0003", UserStatus.DORMANT);
        em.persist(userC);
        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.SUSPENDED);
        em.persist(userD);

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers.size()).isEqualTo(4);
        assertThat(allUsers).extracting("name").containsExactly("A", "B", "C", "D");
    }


}