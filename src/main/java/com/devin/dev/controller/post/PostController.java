package com.devin.dev.controller.post;

import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/postlist/posts")
    public DefaultResponse<?> getPostList(@RequestParam(defaultValue = "-1") Long id,
                                          @PageableDefault(page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostInfoList(id, pageable);
    }

    @GetMapping("/postlist/{id}")
    public DefaultResponse<PostDetailsDto> getPostDetails(
            @PathVariable("id") Long postId,
            @RequestParam(defaultValue = "true", name = "sort_reply") boolean sort_reply,
            HttpServletRequest request) {
        ReplyOrderCondition replyOrderCondition = new ReplyOrderCondition();
        if (!sort_reply) {
            replyOrderCondition.setLatestDate(true);
        } else {
            replyOrderCondition.setLikeCount(true);
        }
        return postService.getPost(postId, replyOrderCondition, request);
    }

    @GetMapping("/postlist")
    public DefaultResponse<?> getPostListByCondition(
            PostSearchCondition condition,
            @PageableDefault(page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostInfoListByCondition(condition, pageable);
    }

    @DeleteMapping("/post/{id}")
    public DefaultResponse<PostDetailsDto> deletePost(@PathVariable("id") Long postId) {
        return postService.deletePost(postId);
    }

    @PostMapping("/post")
    public DefaultResponse<?> post(@RequestBody PostForm form, HttpServletRequest request) {
        return postService.post(form, request);
    }

    @PatchMapping("/post/{id}/like")
    public DefaultResponse<?> changePostLike(@PathVariable("id") Long postId, HttpServletRequest request) {
        return postService.changePostLike(postId, request);
    }

}
