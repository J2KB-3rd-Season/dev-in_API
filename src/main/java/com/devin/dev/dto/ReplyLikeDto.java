package com.devin.dev.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class ReplyLikeDto {

    private Long replyId;
    private Long userId;
    private String username;

    @QueryProjection
    public ReplyLikeDto(Long replyId, Long userId, String username) {
        this.replyId = replyId;
        this.userId = userId;
        this.username = username;
    }
}
