package com.devin.dev.controller.post;

import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/postlist/posts")
    public DefaultResponse<?> getPostList(@RequestParam(defaultValue = "-1")Long id,
          @PageableDefault(page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostInfoList(id, pageable);
    }

    @GetMapping("/postlist/{id}")
    public DefaultResponse<PostDetailsDto> findReplies(
            @PathVariable("id") Long postId,
            @RequestParam(defaultValue = "true", name = "sort_reply") boolean sort_reply) {
        ReplyOrderCondition replyOrderCondition = new ReplyOrderCondition();
        if (!sort_reply) {
            replyOrderCondition.setLatestDate(true);
        } else {
            replyOrderCondition.setLikeCount(true);
        }
        return postService.getPost(postId, replyOrderCondition);
    }

}
