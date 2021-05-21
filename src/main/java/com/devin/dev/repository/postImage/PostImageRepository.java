package com.devin.dev.repository.postImage;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageRepositoryQuery {

}
