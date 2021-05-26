package com.devin.dev.repository.post;

import com.devin.dev.controller.post.PostSearchCondition;
import com.devin.dev.dto.post.PostDto;
import com.devin.dev.dto.post.QPostDto;
import com.devin.dev.entity.post.*;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.user.QUser;
import com.devin.dev.entity.user.User;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devin.dev.entity.post.QPost.post;
import static com.devin.dev.entity.post.QPostTag.postTag;
import static com.devin.dev.entity.post.QSubject.subject;
import static com.devin.dev.entity.reply.QReply.reply;
import static com.devin.dev.entity.user.QUser.user;
import static org.springframework.util.StringUtils.hasText;

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

    /*
    select post.title, post.user.name, post.content, post.status
    from post
    where post.tag like tag
    and post.name like name
    and post.user.name like username

     */
    @Override
    public Page<PostDto> findPostDtoPageWithCondition(PostSearchCondition condition, Pageable pageable) {
        QueryResults<PostDto> results = queryFactory
                .select(new QPostDto(
                        post.title,
                        user.name,
                        post.content,
                        post.status
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        usernameLike(condition.getUsername()),
                        titleLike(condition.getTitle()),
                        tagsInclude(condition.getTags())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<PostDto> content = results.getResults();
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
                .select(new QPostDto(post.title, post.user.name, post.content, post.status)) // QDTO 생성자 사용. @Projections 및 Q파일 컨파일
                .from(post)
                .where(post.user.eq(user))
                .fetch();
    }

    private BooleanExpression usernameLike(String username) {
        return hasText(username) ? user.name.like(username) : null;
    }

    private BooleanExpression titleLike(String title) {
        return hasText(title) ? post.title.like(title) : null;
    }

    private BooleanExpression tagsInclude(List<String> tags) {
        if(tags == null)
            return null;

        JPAQuery<PostTag> postTags = queryFactory
                .select(postTag)
                .from(postTag)
                .leftJoin(postTag.tag, subject)
                .where(subject.name.in(tags));

        return post.tags.contains(postTags);
    }
}
