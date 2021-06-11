package com.devin.dev.dto.reply;

import com.devin.dev.entity.reply.Reply;
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

    private Long id;
    private String name;
    private String content;
    private ReplyStatus status;
    private Integer like;
    private List<String> images = new ArrayList<>();

    @QueryProjection
    public ReplyDto(Long id, String name, String content, ReplyStatus status, Integer like) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.status = status;
        this.like = like;
    }

    public ReplyDto(Reply reply) {
        this.id = reply.getId();
        this.name = reply.getUser().getName();
        this.content = reply.getContent();
        this.status = reply.getStatus();
        this.like = reply.getLikes().size();
    }
}
