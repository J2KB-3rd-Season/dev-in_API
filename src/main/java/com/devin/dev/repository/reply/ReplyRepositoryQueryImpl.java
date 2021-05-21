package com.devin.dev.repository.reply;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.devin.dev.entity.reply.QReply.reply;

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






}
