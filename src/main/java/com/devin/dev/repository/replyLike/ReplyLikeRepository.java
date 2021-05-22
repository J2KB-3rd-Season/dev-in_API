package com.devin.dev.repository.replyLike;

import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long>, ReplyLikeRepositoryQuery {

}
