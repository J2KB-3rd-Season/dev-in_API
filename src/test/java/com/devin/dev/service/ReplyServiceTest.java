package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.reply.ReplyRepository;
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
class ReplyServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @Rollback(false) // 테스트 롤백 안하고싶으면
    void replyTest() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        Long replyId = replyService.reply(replyUser.getId(), post1.getId(), "reply_content", List.of("i1", "i2", "i3"));

        Reply foundReply = em.find(Reply.class, replyId);

        assertThat(foundReply.getImages()).extracting("path").containsExactly("i1", "i2", "i3");
        assertThat(foundReply.getUser()).isEqualTo(replyUser);
        assertThat(foundReply.getPost()).isEqualTo(post1);
        assertThat(foundReply.getContent()).isEqualTo("reply_content");

    }

}