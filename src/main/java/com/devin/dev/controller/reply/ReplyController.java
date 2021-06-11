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

    @PostMapping("/reply")
    public DefaultResponse<?> reply(@RequestBody @Valid ReplyForm form, HttpServletRequest request) {
        return replyService.reply(form, request);
    }

    @GetMapping("/reply/{id}")
    public Page<ReplyDto> findReplies(
            @PathVariable("id") Long postId, Pageable pageable) {
        DefaultResponse<Page<ReplyDto>> response = replyService.findRepliesInPost(postId, pageable);
        return response.getData();
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
