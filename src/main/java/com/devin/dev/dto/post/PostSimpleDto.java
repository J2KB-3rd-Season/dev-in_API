package com.devin.dev.dto.post;

import com.devin.dev.entity.post.PostStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PostSimpleDto {

    private String title;
    private String username;
    private String content;
    private PostStatus status;
    private Integer replyQuantity;

    @QueryProjection
    public PostSimpleDto(String title, String username, String content, PostStatus status, Integer replyQuantity) {
        this.title = title;
        this.username = username;
        this.content = content;
        this.status = status;
        this.replyQuantity = replyQuantity;
    }
}
