package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
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

    @Transactional
    public Long reply(Long userId, Long postId, String content, List<String> imagePaths) throws IllegalStateException {
        // 엔티티 조회. 실패시 IllegalStateException
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저 조회 실패"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("게시글 조회 실패"));

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

}
