package com.devin.dev.repository.post;

import com.devin.dev.dto.post.PostDto;
import com.devin.dev.dto.post.QPostDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.user.User;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devin.dev.entity.post.QPost.post;
import static com.devin.dev.entity.reply.QReply.reply;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public Page<String> findPostnamePageByUser(User user, Pageable pageable) {
        QueryResults<String> results = queryFactory
                .select(post.title)
                .from(post)
                .where(post.user.eq(user))
                .orderBy(post.lastModifiedDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<String> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Post> findPostByUser(User user) {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.user.eq(user))
                .fetch();
    }

    @Override
    public List<PostDto> findPostDtoByUser(User user) {
        return queryFactory
                .select(new QPostDto(post.title, post.content, post.state)) // QDTO 생성자 사용. @Projections 및 Q파일 컨파일
                .from(post)
                .where(post.user.eq(user))
                .fetch();
    }

}
