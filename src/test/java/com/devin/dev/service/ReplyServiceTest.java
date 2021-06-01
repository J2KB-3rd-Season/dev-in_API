package com.devin.dev.service;

import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.dto.reply.ReplyLikeDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
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
    void replySuccess() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        DefaultResponse<ReplyDto> response = replyService.reply(replyUser.getId(), post1.getId(),
                "reply_content", List.of("i1", "i2", "i3"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.REPLY_UPLOAD_SUCCESS);

        Reply foundReply = em.find(Reply.class, response.getData().getId());

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
    void replyWrongUserFailed() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        DefaultResponse<ReplyDto> response = replyService.reply(-1L, post1.getId(),
                "reply_content", List.of("i1", "i2", "i3"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.NOT_FOUND_USER);

        assertThat(response.getData()).isNull();
    }

    @Test
    void replyWrongPostFailed() {
        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        User replyUser = new User("E", "e@b.com", "passE", "0005", UserStatus.ACTIVE);
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        em.persist(postUser);
        em.persist(post1);
        em.persist(replyUser);

        DefaultResponse<ReplyDto> response = replyService.reply(replyUser.getId(), -1L,
                "reply_content", List.of("i1", "i2", "i3"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.NOT_FOUND_POST);

        assertThat(response.getData()).isNull();
    }

    @Test
    void replyLikeSuccess() {
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

        DefaultResponse<ReplyLikeDto> response = replyService.changeReplyLike(likeUser.getId(), reply.getId());
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS);

        ReplyLike replyLike = em.find(ReplyLike.class, response.getData().getId());

        // 좋아요 누른 사람 경험치 1 증가 검증
        assertThat(replyLike.getUser()).isEqualTo(likeUser);
        assertThat(replyLike.getUser().getExp()).isEqualTo(1);

        replyUser = em.find(User.class, replyUser.getId());

        // 답변 작성자 경험치 1 증가 검증
        assertThat(replyUser.getExp()).isEqualTo(1);

    }

    @Test
    void replyCancelLikeSuccess() {
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
        replyLike.changeReply(reply);
        replyLike.setUser(likeUser);
        em.persist(replyLike);

        // 좋아요 취소 검증
        DefaultResponse<ReplyLikeDto> response = replyService.changeReplyLike(likeUser.getId(), reply.getId());
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS);

        assertThat(replyLike.getId()).isEqualTo(response.getData().getId());

        likeUser = em.find(User.class, likeUser.getId());

        // 좋아요 취소한 사람 경험치 1 증가 검증 (3 -> 2)
        assertThat(likeUser.getExp()).isEqualTo(2);

        replyUser = em.find(User.class, replyUser.getId());

        // 답변 작성자 경험치 1 감소 검증 (3 -> 2)
        assertThat(replyUser.getExp()).isEqualTo(2);

    }

    @Test
    void editReplySuccess() {
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

        DefaultResponse<ReplyDto> response = replyService.editReply(replyUser.getId(), reply.getId(),
                "edited_content", List.of("i3", "i1", "i2"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.REPLY_EDIT_SUCCESS);

        Reply foundReply = em.find(Reply.class, response.getData().getId());


        // 답변 검증
        assertThat(foundReply.getImages()).extracting("path").containsExactly("i3", "i1", "i2");
        assertThat(foundReply.getUser().getName()).isEqualTo("E");
        assertThat(foundReply.getPost().getContent()).isEqualTo(post1.getContent());
        assertThat(foundReply.getContent()).isEqualTo("edited_content");
    }

    @Test
    void editReplyWrongUserFailed() {
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

        // 다른 유저로 댓글 수정 요청, 실패 response 객체 확인
        DefaultResponse<ReplyDto> response = replyService.editReply(postUser.getId(), reply.getId(),
                "edited_content", List.of("i3", "i1", "i2"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.NOT_SAME_USER);

        // 안바뀐거 확인
        Reply foundReply = replyRepository.findById(reply.getId()).get();
        assertThat(foundReply.getImages()).extracting("path").containsExactly("i1", "i2", "i3");
        assertThat(foundReply.getUser()).isEqualTo(replyUser);
        assertThat(foundReply.getPost()).isEqualTo(post1);
        assertThat(foundReply.getContent()).isEqualTo("reply_content");
    }


}