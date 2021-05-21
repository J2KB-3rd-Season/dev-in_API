package com.devin.dev.repository.reply;

import com.devin.dev.dto.QReplyLikeDto;
import com.devin.dev.dto.ReplyLikeDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.devin.dev.entity.reply.QReply.reply;
import static com.devin.dev.entity.reply.QReplyLike.replyLike;

@RequiredArgsConstructor
public class ReplyRepositoryQueryImpl implements ReplyRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reply> findReplyPageByPost(Post post, Pageable pageable) {
        return queryFactory
                .select(reply)
                .from(reply)
                .orderBy(reply.lastModifiedDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Optional<ReplyLikeDto> findReplyLikeByReplyAndUser(Long replyId, User user) {
//        Expressions.asBoolean(true).isTrue() : BooleanExpression 값 설정
        return Optional.ofNullable(queryFactory
                .select(new QReplyLikeDto(
                        reply.id.as("replyId"),
                        replyLike.user.id.as("userId"),
                        replyLike.user.name.as("username")
                ))
                .from(reply)
                .leftJoin(reply.likes, replyLike)
                .where(reply.user.eq(user)
                .and(reply.id.eq(replyId)))
                .fetchFirst());
    }

    @Override
    public List<ReplyLikeDto> findReplyLikesById(Long replyId) {
        return queryFactory
                .select(new QReplyLikeDto(
                        reply.id.as("replyId"),
                        replyLike.user.id.as("userId"),
                        replyLike.user.name.as("username")
                ))
                .from(reply)
                .leftJoin(reply.likes, replyLike)
                .where(reply.id.eq(replyId))
                .fetch();
    }

    @Override
    public Long findReplyLikeCountById(Long replyId) {
        return queryFactory
                .select(replyLike.id)
                .from(reply)
                .where(reply.id.eq(replyId))
                .fetchCount();
    }


}
