package com.devin.dev.sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HelloRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    HelloJpaRepository helloJpaRepository;

    @BeforeEach
    public void initData() {
        Hello a = new Hello("a");
        Hello b = new Hello("b");
        Hello c = new Hello("c");
        Hello d = new Hello("d");

        em.persist(a);
        em.persist(b);
        em.persist(c);
        em.persist(d);
    }

    @Test
    public void findAll() {
        List<Hello> allHello = helloJpaRepository.findAll();
        assertThat(allHello)
                .extracting("username")
                .containsExactly("a", "b", "c", "d");
    }

    @Test
    public void findByName() {
        Hello e = new Hello("e");
        em.persist(e);

        Hello findHello = helloJpaRepository.findById(e.getId()).get();
        assertThat(findHello).isEqualTo(e);
    }

    @Test
    public void save() {
        Hello e = new Hello("e");

        helloJpaRepository.save(e);

        Hello findHello = em.find(Hello.class, e.getId());
        assertThat(findHello).isEqualTo(e);
    }

    @Test
    public void deleteOne() {
        Hello e = new Hello("e");
        em.persist(e);

        helloJpaRepository.delete(e);
        assertThat(em.find(Hello.class, e.getId())).isNull();
    }
}