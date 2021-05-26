package com.devin.dev.repository.post;

import com.devin.dev.entity.post.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
