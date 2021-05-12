package com.devin.dev.repository.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostState;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {

    List<Post> findByUserAndState(User user, PostState state);

    List<Post> findByTagsInAndState(List<PostTag> tags, PostState state);

    List<Post> findByTitleLikeAndState(String title, PostState state);

    List<Post> findByTitleLikeOrContentLikeAndState(String title, String content, PostState state);


}
