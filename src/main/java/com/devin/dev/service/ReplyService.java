package com.devin.dev.service;

import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.dto.reply.ReplyLikeDto;
import com.devin.dev.dto.reply.ReplyMapper;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyImageRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final ReplyImageRepository replyImageRepository;
    private final ReplyLikeRepository replyLikeRepository;

    // 답변 작성
    @Transactional
    public DefaultResponse<ReplyDto> reply(Long userId, Long postId, String content, List<String> imagePaths) throws IllegalArgumentException {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_POST);
        }
        Post post = postOptional.get();

        // 엔티티 생성
        List<ReplyImage> replyImages = ReplyImage.createReplyImages(imagePaths);
        Reply reply = Reply.createReplyWithImages(post, user, replyImages, content);

        // 리플 작성자 경험치증가 (게시글작성자 != 리플작성자 인 경우)
        if (isNotSameUser(post.getUser(), reply.getUser())) {
            user.changeExp(User.ExpChangeType.CREATE_REPLY);
        }

        // 저장
        replyRepository.save(reply);
        replyImageRepository.saveAll(replyImages);

        // DTO 변환
        ReplyDto replyDto = ReplyMapper.replyToReplyDto(reply);

        // response 객체 리턴
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.REPLY_UPLOAD_SUCCESS, replyDto);
    }

    // 답변 수정
    @Transactional
    public DefaultResponse<ReplyDto> editReply(Long userId, Long replyId, String content, List<String> imagePaths) {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        if (isNotSameUser(user, reply.getUser())) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_SAME_USER);
        }

        // 기존 이미지 경로 삭제
        List<ReplyImage> replyImages = replyImageRepository.findByReply(reply);
        replyImageRepository.deleteInBatch(replyImages);

        // 수정된 내용 반영
        List<ReplyImage> newReplyImages = ReplyImage.createReplyImages(imagePaths);
        reply.setContent(content);
        reply.setReplyImages(newReplyImages);

        // 저장
        replyImageRepository.saveAll(newReplyImages);
        replyRepository.save(reply);

        // DTO 변환
        ReplyDto replyDto = ReplyMapper.replyToReplyDto(reply);

        // response 객체 리턴
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.REPLY_EDIT_SUCCESS, replyDto);
    }

    // 좋아요 상태변경
    @Transactional
    public DefaultResponse<ReplyLikeDto> changeReplyLike(Long userId, Long replyId) throws IllegalArgumentException {
        // 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("답변 조회 실패"));

        // 추천 조회
        Optional<ReplyLike> likeOptional = replyRepository.findLikeByUser(reply, user);

        ReplyLike replyLike;
        // 추천 유무에 따라 실행
        if (likeOptional.isPresent()) {
            replyLike = likeOptional.get();
            replyLikeRepository.delete(replyLike);
            if (isNotSameUser(user, reply.getUser())) {
                // 좋아요 누른 사람 경험치 감소
                user.changeExp(User.ExpChangeType.REPLY_CANCEL_LIKE);
                // 답변 작성자 경험치 감소
                reply.getUser().changeExp(User.ExpChangeType.REPLY_NOT_BE_LIKED);
            }
        } else {
            replyLike = reply.like(user, new ReplyLike());
            if (isNotSameUser(user, reply.getUser())) {
                // 좋아요 누른 사람 경험치 증가
                user.changeExp(User.ExpChangeType.REPLY_LIKE);
                // 답변 작성자 경험치 증가
                reply.getUser().changeExp(User.ExpChangeType.REPLY_BE_LIKED);
            }
            replyLikeRepository.save(replyLike);
        }

        ReplyLikeDto replyLikeDto = new ReplyLikeDto(replyLike.getId(), user.getName());

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS, replyLikeDto);
    }

    @Transactional(readOnly = true)
    public Page<ReplyDto> findRepliesInPost(Long postId, Pageable pageable) {
        Page<Reply> replies = replyRepository.findReplyPageByPost(postId, pageable);
        List<ReplyDto> replyDtos = ReplyMapper.toDtos(replies.toList());

        return new PageImpl<>(replyDtos, pageable, replies.getTotalElements());
    }


    private boolean isNotSameUser(User firstUser, User secondUser) {
        return !firstUser.getId().equals(secondUser.getId());
    }

}
