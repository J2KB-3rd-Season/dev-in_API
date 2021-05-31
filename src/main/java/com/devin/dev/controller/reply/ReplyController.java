package com.devin.dev.controller.reply;

import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.ReplyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replies")
    public DefaultResponse<?> saveReply(@RequestBody @Valid CreateReplyRequest request) {
        return replyService.reply(request.getUserId(), request.getPostId(), request.getContent(), request.getReplyImages());
    }

    @GetMapping("/reply/{id}")
    public Page<ReplyDto> findReplies(
            @PathVariable("id") Long postId, Pageable pageable) {
        return replyService.findRepliesInPost(postId, pageable);
    }

    @Data
    private static class CreateReplyResponse {
        private Long id;

        public CreateReplyResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateReplyRequest {
        @NotEmpty
        private Long postId;
        @NotEmpty
        private Long userId;
        @NotEmpty
        private String content;
        @NotEmpty
        private List<String> replyImages;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
