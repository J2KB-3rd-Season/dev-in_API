package com.devin.dev.dto.reply;

import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyStatus;
import com.devin.dev.entity.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReplyDto {

    private String name;
    private String content;
    private ReplyStatus status;
    private Integer like;
    private List<String> images = new ArrayList<>();



    @QueryProjection
    public ReplyDto(String name, String content, ReplyStatus status, Integer like) {
        this.name = name;
        this.content = content;
        this.status = status;
        this.like = like;
    }
}
