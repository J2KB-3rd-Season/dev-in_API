package com.devin.dev.repository.reply;

import com.devin.dev.dto.QReplyLikeDto;
import com.devin.dev.dto.ReplyLikeDto;
import com.devin.dev.dto.reply.QReplyDto;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.entity.reply.QReplyImage;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.devin.dev.entity.reply.QReply.reply;
import static com.devin.dev.entity.reply.QReplyLike.replyLike;

// 세부 쿼리 구현
@RequiredArgsConstructor
public class ReplyRepositoryQueryImpl implements ReplyRepositoryQuery {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     *
     * select reply.username, reply.content, reply.status, replyImage.images
     * from reply
     * left join replyImage set reply.image_id = replyImage.id
     * where reply.post_id = postId
     *
     * @return
     */
    @Override
    public Page<Reply> findReplyPageByPost(Long postId, Pageable pageable) {
        QueryResults<Reply> results = queryFactory
                .select(reply)
                .from(reply)
                .where(reply.post.id.eq(postId))
                .orderBy(reply.lastModifiedDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Reply> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<ReplyLikeDto> findReplyLikeByReplyAndUser(Long replyId, User user) {
//        Expressions.asBoolean(true).isTrue() : BooleanExpression 값 설정
        return Optional.ofNullable(queryFactory
                .select(new QReplyLikeDto(
                        reply.id,
                        replyLike.id,
                        replyLike.user.id,
                        replyLike.user.name.as("username")
                ))
                .from(reply)
                .leftJoin(reply.likes, replyLike)
                .where(
                        replyLike.user.eq(user),
                        reply.id.eq(replyId)
                )
                .fetchOne());
    }

    @Override
    public List<ReplyLikeDto> findReplyLikesById(Long replyId) {
        return queryFactory
                .select(new QReplyLikeDto(
                        reply.id,
                        replyLike.id,
                        replyLike.user.id,
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

    @Override
    public Optional<ReplyLike> findReplyLikeByLikeId(Long replyLikeId) {
        return Optional.ofNullable(queryFactory
                .select(replyLike)
                .from(replyLike)
                .where(replyLike.id.eq(replyLikeId))
                .fetchOne());
    }

    @Override
    public ReplyLike findLikeByUser(Reply inputReply, User inputUser) {
        return queryFactory
                .selectFrom(replyLike)
                .where(
                        replyLike.reply.eq(inputReply),
                        replyLike.user.eq(inputUser)
                )
                .fetchOne();
    }


}
