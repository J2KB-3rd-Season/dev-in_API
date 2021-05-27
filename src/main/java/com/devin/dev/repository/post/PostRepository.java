package com.devin.dev.repository.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostStatus;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {

    List<Post> findByUserAndStatus(User user, PostStatus status);

    List<Post> findByTitleLikeAndStatus(String title, PostStatus status);

    List<Post> findByTitleLikeOrContentLikeAndStatus(String title, String content, PostStatus status);

    List<Post> findByTitle(@NotNull String title);

    List<Post> findByUser(User user);

}
