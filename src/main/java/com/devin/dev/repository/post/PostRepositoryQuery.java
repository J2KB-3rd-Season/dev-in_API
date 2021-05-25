package com.devin.dev.repository.post;

import com.devin.dev.dto.post.PostDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;

import java.util.List;

public interface PostRepositoryQuery {

    List<String> findPostnameByUser(User user);

    List<Post> findPostByUser(User user);

    List<PostDto> findPostDtoByUser(User user);
}
