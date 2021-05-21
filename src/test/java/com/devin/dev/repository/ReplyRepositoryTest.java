package com.devin.dev.repository;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReplyRepository replyRepository;

    @BeforeEach
    void setDummyData() {
        User userA = new User("A", "a@b.com", "passA", "0001", UserStatus.ACTIVE);
        Post postA1 = new Post(userA, "PostA1", "ContentA1");
        Post postA2 = new Post(userA, "PostA2", "ContentA2");
        userA.getPosts().add(postA1);
        userA.getPosts().add(postA2);
        em.persist(postA1);
        em.persist(postA2);
        em.persist(userA);


        User userB = new User("B", "b@b.com", "passB", "0002", UserStatus.DELETED);
        Post postB1 = new Post(userB, "PostB1", "ContentB1");
        Post postB2 = new Post(userB, "PostB2", "ContentB2");
        Post postB3 = new Post(userB, "PostB3", "ContentB3");
        Post postB4 = new Post(userB, "PostB4", "ContentB4");
        userB.getPosts().add(postB1);
        userB.getPosts().add(postB2);
        userB.getPosts().add(postB3);
        userB.getPosts().add(postB4);
        em.persist(postB1);
        em.persist(postB2);
        em.persist(postB3);
        em.persist(postB4);
        em.persist(userB);


        User userC = new User("C", "c@b.com", "passC", "0003", UserStatus.DORMANT);
        Post postC1 = new Post(userC, "PostC1", "ContentC1");
        Post postC2 = new Post(userC, "PostC2", "ContentC2");
        Post postC3 = new Post(userC, "PostC3", "ContentC3");
        userC.getPosts().add(postC1);
        userC.getPosts().add(postC2);
        userC.getPosts().add(postC3);
        em.persist(postC1);
        em.persist(postC2);
        em.persist(userC);

    }

    @Test
    void postReply() {
        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        Post postD1 = new Post(userD, "PostC1", "ContentC1");

        em.persist(userD);
        em.persist(postD1);

        Reply reply = Reply.createReply(postD1, userD, "reply_content1");

        replyRepository.save(reply);

        Reply foundReply = em.find(Reply.class, reply.getId());

        assertThat(reply).isEqualTo(foundReply);
        assertThat(reply.getUser()).isEqualTo(userD);

    }

    @Test
    void viewFirstPageReply() {
        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        Post postD1 = new Post(userD, "PostC1", "ContentC1");

        em.persist(userD);
        em.persist(postD1);

        for (int i = 0; i < 10; i++) {
            Reply reply = Reply.createReply(postD1, userD, "reply_content" + i);
            em.persist(reply);
        }

        Pageable pageable = PageRequest.of(0, 4);
        List<Reply> firstPageReplies = replyRepository.findReplyPageByPost(postD1, pageable);

        assertThat(firstPageReplies).allMatch(r -> r.getUser().getId().equals(userD.getId()));

        assertThat(firstPageReplies)
                .extracting("content")
                .containsExactly(
                        "reply_content0",
                        "reply_content1",
                        "reply_content2",
                        "reply_content3"
                );

        List<Reply> allReplies = replyRepository.findByPost(postD1);

        assertThat(allReplies)
                .extracting("content")
                .containsExactly(
                        "reply_content0",
                        "reply_content1",
                        "reply_content2",
                        "reply_content3",
                        "reply_content4",
                        "reply_content5",
                        "reply_content6",
                        "reply_content7",
                        "reply_content8",
                        "reply_content9"
                );

        assertThat(allReplies).allMatch(r -> r.getUser().getId().equals(userD.getId()));
    }

    @Test
    void postReplyWithImages() {
        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        Post postD1 = new Post(userD, "PostC1", "ContentC1");
        em.persist(userD);
        em.persist(postD1);

        List<ReplyImage> replyImages = ReplyImage.createReplyImages(List.of("i1", "i2", "i3"));
        Reply reply = Reply.createReplyWithImages(postD1, userD, replyImages, "reply_content");

        em.persist(reply);

        Reply foundReply = replyRepository.findById(reply.getId()).orElseThrow(IllegalStateException::new);

        assertThat(foundReply.getImages()).extracting("path").containsExactly("i1", "i2", "i3");
        assertThat(foundReply.getUser()).isEqualTo(userD);
        assertThat(foundReply.getPost()).isEqualTo(postD1);
        assertThat(foundReply.getContent()).isEqualTo("reply_content");
    }

}