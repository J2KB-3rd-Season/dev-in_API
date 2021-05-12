package com.devin.dev.repository.reply;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.QReply;
import com.devin.dev.entity.reply.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.devin.dev.entity.reply.QReply.reply;

@RequiredArgsConstructor
public class ReplyRepositoryQueryImpl implements ReplyRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reply> findReplyPageByPost(Post post, int index, int limit) {
        return queryFactory
                .select(reply)
                .from(reply)
                .orderBy(reply.lastModifiedDate.asc())
                .offset(index)
                .limit(limit)
                .fetch();
    }


}
