package com.devin.dev.controller;

import com.devin.dev.service.ReplyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/replies")
    public CreateReplyResponse saveReply(@RequestBody @Valid CreateReplyRequest request) {

        return new CreateReplyResponse();
    }

    @Data
    private static class CreateReplyResponse {

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
        private List<String> reply_images;
    }
}
