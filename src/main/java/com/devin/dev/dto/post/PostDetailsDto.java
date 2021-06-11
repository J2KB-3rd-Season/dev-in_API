package com.devin.dev.dto.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import com.devin.dev.entity.post.PostStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDetailsDto {

    private Long post_id;
    private Long publisher_id;
    private String publisher_name;
    private String publisher_profile;
    private Long publisher_exp;
    private String title;
    private String content;
    private List<String> post_images;
    private List<String> post_tags;
    private boolean status_accept;
    private LocalDateTime date_create;
    private LocalDateTime date_update;
    private Integer reply_num;
    private List<PostReplyDto> reply;

    @QueryProjection
    public PostDetailsDto(Long post_id, Long publisher_id, String publisher_name, String publisher_profile, Long publisher_exp, String title, String content, List<String> post_images, List<String> post_tags, boolean status_accept, boolean like_post, LocalDateTime date_create, LocalDateTime date_update, Integer reply_num, List<PostReplyDto> replyDtos) {
        this.post_id = post_id;
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.publisher_profile = publisher_profile;
        this.publisher_exp = publisher_exp;
        this.title = title;
        this.content = content;
        this.post_images = post_images;
        this.post_tags = post_tags;
        this.status_accept = status_accept;
        this.date_create = date_create;
        this.date_update = date_update;
        this.reply_num = reply_num;
        this.reply = replyDtos;
    }

    public PostDetailsDto(Post post) {
        this.post_id = post.getId();
        this.publisher_id = post.getUser().getId();
        this.publisher_name = post.getUser().getName();
        this.publisher_profile = post.getUser().getProfile();
        this.publisher_exp = post.getUser().getExp();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.post_images = post.getImages().stream().map(PostImage::getPath).collect(Collectors.toList());
        this.post_tags = post.getPostTags();
        this.status_accept = post.getStatus() == PostStatus.SELECTED;
        this.date_create = post.getCreatedDate();
        this.date_update = post.getLastModifiedDate();
        this.reply_num = post.getReplies().size();
        this.reply = post.getReplies().stream().map(PostReplyDto::new).collect(Collectors.toList());
    }
}
