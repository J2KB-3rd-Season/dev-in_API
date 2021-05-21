package com.devin.dev.repository.replyImage;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.devin.dev.entity.reply.QReply.reply;

@RequiredArgsConstructor
public class ReplyImageRepositoryQueryImpl implements ReplyImageRepositoryQuery {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

}
