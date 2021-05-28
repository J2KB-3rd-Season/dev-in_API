package com.devin.dev.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class PostReplyDto {

    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private List<String> imagePaths;

    @QueryProjection
    public PostReplyDto(Long postId, Long userId, String username, String content, List<String> imagePaths) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.imagePaths = imagePaths;
    }
}
