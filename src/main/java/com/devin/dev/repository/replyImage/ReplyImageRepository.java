package com.devin.dev.repository.replyImage;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyImageRepository extends JpaRepository<ReplyImage, Long>, ReplyImageRepositoryQuery {

    List<ReplyImage> findByReply(Reply reply);

}
