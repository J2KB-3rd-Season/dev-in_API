package com.devin.dev.repository.reply;

import com.devin.dev.dto.QReplyLikeDto;
import com.devin.dev.dto.ReplyLikeDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.QReplyRecommend;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyRecommend;
import com.devin.dev.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.devin.dev.entity.reply.QReply.reply;
import static com.devin.dev.entity.reply.QReplyRecommend.replyRecommend;
//import static com.devin.dev.entity.reply.QReplyLike.replyLike;

@RequiredArgsConstructor
public class ReplyRepositoryQueryImpl implements ReplyRepositoryQuery {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

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
                        reply.id,
                        replyRecommend.id,
                        replyRecommend.user.id,
                        replyRecommend.user.name.as("username")
                ))
                .from(reply)
                .leftJoin(reply.replyRecommends, replyRecommend)
                .where(
                        replyRecommend.user.eq(user),
                        reply.id.eq(replyId)
                )
                .fetchOne());
    }

    @Override
    public List<ReplyLikeDto> findReplyLikesById(Long replyId) {
        return queryFactory
                .select(new QReplyLikeDto(
                        reply.id,
                        replyRecommend.id,
                        replyRecommend.user.id,
                        replyRecommend.user.name.as("username")
                ))
                .from(reply)
                .leftJoin(reply.replyRecommends, replyRecommend)
                .where(reply.id.eq(replyId))
                .fetch();
    }

    @Override
    public Long findReplyLikeCountById(Long replyId) {
        return queryFactory
                .select(replyRecommend.id)
                .from(reply)
                .where(reply.id.eq(replyId))
                .fetchCount();
    }

    @Override
    public Optional<ReplyRecommend> findReplyLikeByLikeId(Long replyLikeId) {
        return Optional.ofNullable(queryFactory
                .select(replyRecommend)
                .from(replyRecommend)
                .where(replyRecommend.id.eq(replyLikeId))
                .fetchOne());
    }

    @Override
    public void deleteLike(ReplyRecommend replyRecommend) {
        em.remove(replyRecommend);
    }

    @Override
    public ReplyRecommend findLikeByUser(Reply inputReply, User inputUser) {
        return queryFactory
                .selectFrom(replyRecommend)
                .where(
                        replyRecommend.reply.eq(inputReply),
                        replyRecommend.user.eq(inputUser)
                )
                .fetchOne();
    }

    @Override
    public void saveLike(ReplyRecommend replyRecommend) {
        em.persist(replyRecommend);
    }


}
