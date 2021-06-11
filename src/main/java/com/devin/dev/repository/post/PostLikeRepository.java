package com.devin.dev.repository.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostLike;
import com.devin.dev.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
