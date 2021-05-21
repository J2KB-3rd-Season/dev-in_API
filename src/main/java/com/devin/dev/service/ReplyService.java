package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.replyImage.ReplyImageRepository;
import com.devin.dev.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final ReplyImageRepository replyImageRepository;

    // 답변 작성
    @Transactional
    public Long reply(Long userId, Long postId, String content, List<String> imagePaths) throws IllegalArgumentException {
        // 엔티티 조회. 실패시 IllegalArgumentException
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));

        // 엔티티 생성
        List<ReplyImage> replyImages = ReplyImage.createReplyImages(imagePaths);
        Reply reply = Reply.createReplyWithImages(post, user, replyImages, content);

        // 리플 작성자 경험치증가
        user.changeExp(User.ExpChangeType.CREATE_REPLY);

        // 저장
        replyRepository.save(reply);

        // reply_id 리턴
        return reply.getId();
    }

    // 답변 수정
    @Transactional
    public Long editReply(Long userId, Long replyId, String content, List<String> imagePaths) {
        // 엔티티 조회. 실패시 IllegalArgumentException
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("답변 조회 실패"));

        return 0L;
//        Reply.setReplyImages();
    }

    // 좋아요 상태변경
    @Transactional
    public Long changeReplyLike(Long userId, Long replyId) throws IllegalArgumentException {
        // 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("답변 조회 실패"));

        // 추천 조회
        ReplyLike replyLike = replyRepository.findLikeByUser(reply, user);

        Long likeId;
        // 추천 유무에 따라 실행
        if(replyLike != null) {
            likeId = replyLike.getId();
            replyRepository.deleteLike(replyLike);
            // 좋아요 누른 사람 경험치 감소
            user.changeExp(User.ExpChangeType.REPLY_CANCEL_LIKE);
            // 답변 작성자 경험치 감소
            reply.getUser().changeExp(User.ExpChangeType.REPLY_NOT_BE_LIKED);
        } else {
            replyLike = reply.like(user, new ReplyLike());
            likeId = replyLike.getId();
            // 좋아요 누른 사람 경험치 증가
            user.changeExp(User.ExpChangeType.REPLY_LIKE);
            // 답변 작성자 경험치 증가
            reply.getUser().changeExp(User.ExpChangeType.REPLY_BE_LIKED);
            replyRepository.saveLike(replyLike);
        }

        return likeId;
    }

}
