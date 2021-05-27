package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.Subject;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Test
    @Rollback(false)
    void postTest() {
        Subject subject1 = new Subject("s1");
        Subject subject2 = new Subject("s2");
        Subject subject3 = new Subject("s3");
        em.persist(subject1);
        em.persist(subject2);
        em.persist(subject3);

        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        em.persist(postUser);

        postService.post(postUser.getId(), "titleA1", "postA1", List.of("s1", "s2") ,List.of("p1", "p2", "p3"));

        List<Post> posts = postRepository.findByTitle("titleA1");

        assertThat(posts).extracting("title").containsExactly("titleA1");
        assertThat(posts.get(0).getTags()).extracting("tag").containsExactly(subject1, subject2);
        assertThat(posts.get(0).getImages()).extracting("path").containsExactly("p1", "p2", "p3");
    }
}