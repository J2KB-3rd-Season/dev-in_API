package com.devin.dev.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PostDto {

    String title;
    String content;

    @QueryProjection
    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
