package com.devin.dev.repository.reply;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyRepositoryQuery {



    List<Reply> findReplyPageByPost(Post post, Pageable pageable);


}
