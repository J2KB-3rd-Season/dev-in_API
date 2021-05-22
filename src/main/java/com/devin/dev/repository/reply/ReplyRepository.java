package com.devin.dev.repository.reply;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA 지원하는 간단한 쿼리 메서드 작성
public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryQuery {

    List<Reply> findByPost(Post post);

}
