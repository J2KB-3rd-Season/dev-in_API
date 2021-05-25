package com.devin.dev.repository.reply;

import com.devin.dev.dto.ReplyLikeDto;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

// 세부 쿼리 인터페이스
public interface ReplyRepositoryQuery {

    Page<Reply> findReplyPageByPost(Long postId, Pageable pageable);

    Optional<ReplyLikeDto> findReplyLikeByReplyAndUser(Long replyId, User user);

    List<ReplyLikeDto> findReplyLikesById(Long replyId);

    Long findReplyLikeCountById(Long replyId);

    Optional<ReplyLike> findReplyLikeByLikeId(Long replyLikeId);

    ReplyLike findLikeByUser(Reply inputReply, User inputUser);

}
