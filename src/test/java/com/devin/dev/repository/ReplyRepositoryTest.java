package com.devin.dev.repository;

import com.devin.dev.dto.ReplyLikeDto;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.dto.reply.ReplyMapper;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    ReplyLikeRepository replyLikeRepository;

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
        Page<Reply> firstPageReplies = replyRepository.findReplyPageByPost(postD1.getId(), pageable);

        assertThat(firstPageReplies).allMatch(r -> r.getUser().getName().equals(userD.getName()));

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
    void viewFirstPageReplyDto() {
        User userD = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        Post postD1 = new Post(userD, "PostC1", "ContentC1");

        em.persist(userD);
        em.persist(postD1);

        for (int i = 0; i < 10; i++) {
            Reply reply = Reply.createReply(postD1, userD, "reply_content" + i);
            List<ReplyImage> replyImages = ReplyImage.createReplyImages(List.of("1", "2", "3"));
            for (ReplyImage replyImage : replyImages) {
                em.persist(replyImage);
            }
            reply.setImages(replyImages);
            em.persist(reply);
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<Reply> firstPageReplies = replyRepository.findReplyPageByPost(postD1.getId(), pageable);

        List<ReplyDto> replyDtos = ReplyMapper.toDtos(firstPageReplies.toList());

        assertThat(replyDtos)
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

    @Test
    void replyLike() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        User replyLikeUser1 = new User("L1", "l1@b.com", "passL1", "0006", UserStatus.ACTIVE);
        User replyLikeUser2 = new User("L2", "l2@b.com", "passL2", "0007", UserStatus.ACTIVE);
        User replyLikeUser3 = new User("L3", "l3@b.com", "passL3", "0008", UserStatus.ACTIVE);
        User replyLikeUser4 = new User("L4", "l4@b.com", "passL4", "0009", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(replyUser);
        em.persist(replyLikeUser1);
        em.persist(replyLikeUser2);
        em.persist(replyLikeUser3);
        em.persist(replyLikeUser4);
        em.persist(post1);

        Reply reply = Reply.createReply(post1, replyUser, "reply_content");

        ReplyLike replyLike1 = new ReplyLike();
        ReplyLike replyLike2 = new ReplyLike();
        ReplyLike replyLike3 = new ReplyLike();
        ReplyLike replyLike4 = new ReplyLike();

        reply.like(replyLikeUser1, replyLike1);
        reply.like(replyLikeUser2, replyLike2);
        reply.like(replyLikeUser3, replyLike3);
        reply.like(replyLikeUser4, replyLike4);
        em.persist(replyLike1);
        em.persist(replyLike2);
        em.persist(replyLike3);
        em.persist(replyLike4);
        em.persist(reply);

        Optional<ReplyLikeDto> like1 = replyRepository.findReplyLikeByReplyAndUser(reply.getId(), replyLikeUser1);
        Optional<ReplyLikeDto> like5 = replyRepository.findReplyLikeByReplyAndUser(reply.getId(), postUser);

        assertThat(like1.get().getUsername()).isEqualTo("L1");
        assertThat(like5).isEmpty();

        List<ReplyLikeDto> replyLikes = replyRepository.findReplyLikesById(reply.getId());

        assertThat(replyLikes.size()).isEqualTo(4);
        assertThat(replyLikes).extracting("username")
                .containsExactly(
                        replyLikeUser1.getName(),
                        replyLikeUser2.getName(),
                        replyLikeUser3.getName(),
                        replyLikeUser4.getName()
                );
    }

    @Test
    void replyCancelLike() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        User replyLikeUser1 = new User("L1", "l1@b.com", "passL1", "0006", UserStatus.ACTIVE);
        User replyLikeUser2 = new User("L2", "l2@b.com", "passL2", "0007", UserStatus.ACTIVE);
        User replyLikeUser3 = new User("L3", "l3@b.com", "passL3", "0008", UserStatus.ACTIVE);
        User replyLikeUser4 = new User("L4", "l4@b.com", "passL4", "0009", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(replyUser);
        em.persist(replyLikeUser1);
        em.persist(replyLikeUser2);
        em.persist(replyLikeUser3);
        em.persist(replyLikeUser4);
        em.persist(post1);

        Reply reply = Reply.createReply(post1, replyUser, "reply_content");

        ReplyLike replyLike1 = new ReplyLike();
        ReplyLike replyLike2 = new ReplyLike();
        ReplyLike replyLike3 = new ReplyLike();
        ReplyLike replyLike4 = new ReplyLike();

        reply.like(replyLikeUser1, replyLike1);
        reply.like(replyLikeUser2, replyLike2);
        reply.like(replyLikeUser3, replyLike3);
        reply.like(replyLikeUser4, replyLike4);
        em.persist(replyLike1);
        em.persist(replyLike2);
        em.persist(replyLike3);
        em.persist(replyLike4);
        em.persist(reply);

        Optional<ReplyLikeDto> like1 = replyRepository.findReplyLikeByReplyAndUser(reply.getId(), replyLikeUser1);
        Optional<ReplyLikeDto> like5 = replyRepository.findReplyLikeByReplyAndUser(reply.getId(), postUser);

        assertThat(like1.get().getUsername()).isEqualTo("L1");
        assertThat(like5).isEmpty();

        List<ReplyLikeDto> replyLikes = replyRepository.findReplyLikesById(reply.getId());

        // 기존 좋아요 4개
        assertThat(replyLikes.size()).isEqualTo(4);
        assertThat(replyLikes).extracting("username")
                .containsExactly(
                        replyLikeUser1.getName(),
                        replyLikeUser2.getName(),
                        replyLikeUser3.getName(),
                        replyLikeUser4.getName()
                );

        ReplyLike replyLike = replyRepository.findReplyLikeByLikeId(like1.get().getLikeId()).get();

        // 하나 지우면
        replyLikeRepository.delete(replyLike);

        like1 = replyRepository.findReplyLikeByReplyAndUser(reply.getId(), replyLikeUser1);
        assertThat(like1).isEmpty();

        // 3개가 됨
        replyLikes = replyRepository.findReplyLikesById(reply.getId());
        assertThat(replyLikes.size()).isEqualTo(3);
        assertThat(replyLikes).extracting("username")
                .containsExactly(
                        replyLikeUser2.getName(),
                        replyLikeUser3.getName(),
                        replyLikeUser4.getName()
                );
    }
}