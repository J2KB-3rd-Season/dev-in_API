package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ReplyServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    void replyTest() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        Long replyId = replyService.reply(replyUser.getId(), post1.getId(), "reply_content", List.of("i1", "i2", "i3"));

        Reply foundReply = em.find(Reply.class, replyId);

        // 답변 검증
        assertThat(foundReply.getImages()).extracting("path").containsExactly("i1", "i2", "i3");
        assertThat(foundReply.getUser()).isEqualTo(replyUser);
        assertThat(foundReply.getPost()).isEqualTo(post1);
        assertThat(foundReply.getContent()).isEqualTo("reply_content");

        User foundUser = em.find(User.class, replyUser.getId());

        // 답변 작성 유저 경험치 3 증가 검증
        assertThat(foundUser.getExp()).isEqualTo(3);
    }

    @Test
    void replyLikeTest() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        User likeUser = new User("F", "f@b.com", "passF", "0006", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);
        em.persist(likeUser);

        Reply reply = Reply.createReply(post1, replyUser, "reply_content");
        em.persist(reply);

        Long replyLikeId = replyService.changeReplyLike(likeUser.getId(), reply.getId());

        ReplyLike replyLike = em.find(ReplyLike.class, replyLikeId);

        // 좋아요 누른 사람 경험치 1 증가 검증
        assertThat(replyLike.getUser()).isEqualTo(likeUser);
        assertThat(replyLike.getUser().getExp()).isEqualTo(1);

        replyUser = em.find(User.class, replyUser.getId());

        // 답변 작성자 경험치 1 증가 검증
        assertThat(replyUser.getExp()).isEqualTo(1);

    }

    @Test
    void replyCancelLikeTest() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        User likeUser = new User("F", "f@b.com", "passF", "0006", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        // 임시로 3 증가
        replyUser.changeExp(User.ExpChangeType.CREATE_REPLY);
        likeUser.changeExp(User.ExpChangeType.CREATE_REPLY);
        assertThat(replyUser.getExp()).isEqualTo(3);
        assertThat(likeUser.getExp()).isEqualTo(3);

        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);
        em.persist(likeUser);

        Reply reply = Reply.createReply(post1, replyUser, "reply_content");
        em.persist(reply);

        ReplyLike replyLike = new ReplyLike();
        replyLike.setReply(reply);
        replyLike.setUser(likeUser);
        em.persist(replyLike);

        // 좋아요 취소 검증
        Long replyLikeId = replyService.changeReplyLike(likeUser.getId(), reply.getId());
        assertThat(replyLike.getId()).isEqualTo(replyLikeId);

        likeUser = em.find(User.class, likeUser.getId());

        // 좋아요 취소한 사람 경험치 1 증가 검증 (3 -> 2)
        assertThat(likeUser.getExp()).isEqualTo(2);

        replyUser = em.find(User.class, replyUser.getId());

        // 답변 작성자 경험치 1 감소 검증 (3 -> 2)
        assertThat(replyUser.getExp()).isEqualTo(2);

    }

    @Test
    @Rollback(false)
    void editReplyTest() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        Reply reply = Reply.createReply(post1, replyUser, "reply_content");

        ReplyImage replyImage1 = new ReplyImage("i1");
        ReplyImage replyImage2 = new ReplyImage("i2");
        ReplyImage replyImage3 = new ReplyImage("i3");

        reply.setReplyImages(List.of(replyImage1, replyImage2, replyImage3));

        em.persist(replyImage1);
        em.persist(replyImage2);
        em.persist(replyImage3);
        em.persist(reply);

        Long replyId = replyService.editReply(replyUser.getId(), reply.getId(), "edited_content", List.of("i3", "i1", "i2"));


        Reply foundReply = em.find(Reply.class, replyId);


        // 답변 검증
        assertThat(foundReply.getImages()).extracting("path").containsExactly("i3", "i1", "i2");
        assertThat(foundReply.getUser().getName()).isEqualTo("E");
        assertThat(foundReply.getPost().getContent()).isEqualTo(post1.getContent());
        assertThat(foundReply.getContent()).isEqualTo("edited_content");
    }


}