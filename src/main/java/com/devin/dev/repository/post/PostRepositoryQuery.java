package com.devin.dev.repository.post;

import com.devin.dev.controller.post.PostSearchCondition;
import com.devin.dev.dto.post.PostDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryQuery {

    Page<String> findPostnamePageByUser(User user, Pageable pageable);

    Page<PostDto> findPostDtoPageWithCondition(PostSearchCondition condition, Pageable pageable);

    List<Post> findPostByUser(User user);

    List<PostDto> findPostDtoByUser(User user);
    
}
