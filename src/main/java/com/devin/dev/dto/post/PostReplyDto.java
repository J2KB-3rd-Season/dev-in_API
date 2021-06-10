package com.devin.dev.dto.post;

import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostReplyDto {

    private Long reply_id;
    private Long publisher_id;
    private String publisher_name;
    private String publisher_profile;
    private Long publisher_exp;
    private String content;
    private List<String> reply_images;
    private boolean status_accept;
    private Integer like_num;
    private LocalDateTime date_create;
    private LocalDateTime date_update;

    @QueryProjection
    public PostReplyDto(Long reply_id, Long publisher_id, String publisher_name, String publisher_profile, Long publisher_exp, String content, List<String> reply_images, boolean status_accept, boolean like_reply, Integer like_num, LocalDateTime date_create, LocalDateTime date_update) {
        this.reply_id = reply_id;
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.publisher_profile = publisher_profile;
        this.publisher_exp = publisher_exp;
        this.content = content;
        this.reply_images = reply_images;
        this.status_accept = status_accept;
        this.like_num = like_num;
        this.date_create = date_create;
        this.date_update = date_update;
    }

    public PostReplyDto(Reply reply) {
        this.reply_id = reply.getId();
        this.publisher_id = reply.getUser().getId();
        this.publisher_name = reply.getUser().getName();
        this.publisher_profile = reply.getUser().getProfile();
        this.publisher_exp = reply.getUser().getExp();
        this.content = reply.getContent();
        this.reply_images = reply.getImages().stream().map(ReplyImage::getPath).collect(Collectors.toList());
        this.status_accept = ReplyStatus.SELECTED == reply.getStatus();
        this.like_num = reply.getLikes().size();
        this.date_create = reply.getCreatedDate();
        this.date_update = reply.getLastModifiedDate();
    }
}
