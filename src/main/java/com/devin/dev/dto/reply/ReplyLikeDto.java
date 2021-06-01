package com.devin.dev.dto.reply;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReplyLikeDto {

    private Long id;
    private String name;

    @QueryProjection
    public ReplyLikeDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
