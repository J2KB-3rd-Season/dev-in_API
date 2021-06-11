package com.devin.dev.repository.post;

import com.devin.dev.controller.post.PostSearchCondition;
import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.dto.post.PostInfoDto;
import com.devin.dev.dto.post.PostSimpleDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryQuery {

    Page<String> findPostnamePageByUser(User user, Pageable pageable);

    Page<PostInfoDto> findPostInfoDtoPageByCondition(PostSearchCondition condition, Pageable pageable);

    List<PostSimpleDto> findPostDtoByUser(User user);

    Page<Post> findAllByTagId(Long id, Pageable pageable);

    Optional<PostDetailsDto> findPostDetailsById(Long id, ReplyOrderCondition condition);
}
