package com.devin.dev.controller.reply;

import com.devin.dev.controller.post.ReplyUpdateForm;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.ReplyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PutMapping("/reply/{id}/update")
    public DefaultResponse<?> reply(@PathVariable("id") Long replyId, @RequestBody @Valid ReplyUpdateForm form, HttpServletRequest request) {
        return replyService.editReply(replyId, form, request);
    }

    @DeleteMapping("/reply/{id}")
    public DefaultResponse<?> reply(@PathVariable("id") Long replyId, HttpServletRequest request) {
        return replyService.deleteReply(replyId, request);
    }

    @PatchMapping("/reply/{id}/like")
    public DefaultResponse<?> changePostLike(@PathVariable("id") Long replyId, HttpServletRequest request) {
        return replyService.changeReplyLike(replyId, request);
    }

    @PatchMapping("/reply/{id}/select")
    public DefaultResponse<?> selectReply(@PathVariable("id") Long replyId, HttpServletRequest request) {
        return replyService.selectReply(replyId, request);
    }

    @Data
    private static class CreateReplyResponse {
        private Long id;

        public CreateReplyResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
