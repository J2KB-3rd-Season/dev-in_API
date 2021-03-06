package com.devin.dev.service;

import com.devin.dev.controller.post.ReplyUpdateForm;
import com.devin.dev.controller.reply.ReplyForm;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.dto.reply.ReplyLikeDto;
import com.devin.dev.dto.reply.ReplyMapper;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostStatus;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.reply.ReplyStatus;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyImageRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.security.JwtAuthTokenProvider;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final JwtAuthTokenProvider tokenProvider;

    // 답변 작성
    @Transactional
    public DefaultResponse<ReplyDto> reply(Long userId, Long postId, String content, List<String> imagePaths) throws IllegalArgumentException {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();
        
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_POST);
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
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_UPLOAD_SUCCESS, replyDto);
    }

    // 답변 수정
    @Transactional
    public DefaultResponse<ReplyDto> editReply(Long userId, Long replyId, String content, List<String> imagePaths) {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        if (isNotSameUser(user, reply.getUser())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NOT_SAME_USER);
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
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_EDIT_SUCCESS, replyDto);
    }

    // 좋아요 상태변경
    @Transactional
    public DefaultResponse<ReplyLikeDto> changeReplyLike(Long userId, Long replyId) throws IllegalArgumentException {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

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

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS, replyLikeDto);
    }

    @Transactional
    public DefaultResponse<?> deleteReply(Long userId, Long replyId) {
        // 엔티티 조회. 실패시 response 인스턴스 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        // 작성자 확인
        if (isNotSameUser(user, reply.getUser())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NOT_SAME_USER);
        }

        // 채택된 답변인지 확인
        if (reply.getStatus() == ReplyStatus.SELECTED) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.SELECTED_REPLY);
        }

        // 리플 작성자 경험치 삭제
        user.changeExp(User.ExpChangeType.DELETE_REPLY);

        // 댓글 상태변경 DELETED 후 저장
        reply.setStatus(ReplyStatus.DELETED);
        replyRepository.save(reply);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_DELETE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<Page<ReplyDto>> findRepliesInPost(Long postId, Pageable pageable) {
        Page<Reply> replies = replyRepository.findReplyPageByPost(postId, pageable);
        List<ReplyDto> replyDtos = ReplyMapper.toDtos(replies.toList());

        PageImpl<ReplyDto> page = new PageImpl<>(replyDtos, pageable, replies.getTotalElements());
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.FOUND_POST, page);
    }
    
    private boolean isNotSameUser(User firstUser, User secondUser) {
        return !firstUser.getId().equals(secondUser.getId());
    }

    @Transactional
    public DefaultResponse<?> reply(ReplyForm form, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Post> postOptional = postRepository.findById(form.getId());
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_POST);
        }
        Post post = postOptional.get();

        // 엔티티 생성
        List<ReplyImage> replyImages = ReplyImage.createReplyImages(form.getReply_image());
        Reply reply = Reply.createReplyWithImages(post, user, replyImages, form.getContent());

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
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_UPLOAD_SUCCESS, replyDto);
    }

    @Transactional
    public DefaultResponse<?> editReply(Long replyId, ReplyUpdateForm form, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        if (isNotSameUser(user, reply.getUser())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NOT_SAME_USER);
        }

        if (reply.getStatus() == ReplyStatus.SELECTED) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.SELECTED_REPLY);
        }

        // 기존 이미지 경로 삭제
        List<ReplyImage> replyImages = replyImageRepository.findByReply(reply);
        replyImageRepository.deleteInBatch(replyImages);

        // 수정된 내용 반영
        List<ReplyImage> newReplyImages = ReplyImage.createReplyImages(form.getReply_image());
        reply.setContent(form.getContent());
        reply.setReplyImages(newReplyImages);

        // 저장
        replyImageRepository.saveAll(newReplyImages);
        replyRepository.save(reply);

        // DTO 변환
        ReplyDto replyDto = ReplyMapper.replyToReplyDto(reply);

        // response 객체 리턴
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_EDIT_SUCCESS, replyDto);
    }

    @Transactional
    public DefaultResponse<?> deleteReply(Long replyId, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.LOGIN_FAIL);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        // 작성자 확인
        if (isNotSameUser(user, reply.getUser())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NOT_SAME_USER);
        }

        // 채택된 답변인지 확인
        if (reply.getStatus() == ReplyStatus.SELECTED) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.SELECTED_REPLY);
        }

        // 리플 작성자 경험치 삭제
        user.changeExp(User.ExpChangeType.DELETE_REPLY);

        // 댓글 상태변경 DELETED 후 저장
        reply.setStatus(ReplyStatus.DELETED);
        replyRepository.save(reply);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_DELETE_SUCCESS);
    }

    @Transactional
    public DefaultResponse<ReplyDto> changeReplyLike(Long replyId, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

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

        ReplyDto replyDto = new ReplyDto(reply);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS, replyDto);
    }

    public DefaultResponse<?> selectReply(Long replyId, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (replyOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_REPLY);
        }
        Reply reply = replyOptional.get();

        if (isNotSameUser(user, reply.getPost().getUser())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NOT_SAME_USER);
        }

        if (user.getId().equals(reply.getUser().getId())) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.SAME_USER);
        }

        user.changeExp(User.ExpChangeType.REPLY_SELECT);
        reply.getUser().changeExp(User.ExpChangeType.REPLY_BE_SELECTED);

        reply.setStatus(ReplyStatus.SELECTED);
        reply.getPost().setStatus(PostStatus.SELECTED);

        replyRepository.save(reply);
        userRepository.save(user);
        userRepository.save(reply.getUser());
        postRepository.save(reply.getPost());

        ReplyDto replyDto = new ReplyDto(reply);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.REPLY_LIKE_CHANGE_SUCCESS, replyDto);
    }
}
