package com.devin.dev.repository.post;

import com.devin.dev.dto.post.PostDto;
import com.devin.dev.dto.post.QPostDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.devin.dev.entity.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public List<String> findPostnameByUser(User user) {
        return queryFactory
                .select(post.title)
                .from(post)
                .where(post.user.eq(user))
                .fetch();
    }

    @Override
    public List<Post> findPostByUser(User user) {
        return queryFactory
                .select(post)
                .from(post)
                .where(post.user.eq(user))
                .fetch();
    }

    public List<PostDto> findPostDtoByUserWithProjection(User user) {
        return queryFactory
                .select(Projections.constructor( // Projections.constructor 스태틱 메서드 사용
                        PostDto.class,
                        post.title,
                        post.content))
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
