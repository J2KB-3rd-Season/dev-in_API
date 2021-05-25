package com.devin.dev.dto.post;

import com.devin.dev.entity.post.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class PostDto {

    private String title;
    private String content;
    private PostState state;

    @QueryProjection
    public PostDto(String title, String content, PostState state) {
        this.title = title;
        this.content = content;
        this.state = state;
    }
}
