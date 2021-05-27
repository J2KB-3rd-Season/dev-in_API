package com.devin.dev.repository.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.post.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPost(Post post);

    List<PostTag> findByTagIn(List<Subject> tag);
}
