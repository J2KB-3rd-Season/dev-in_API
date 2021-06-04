package com.devin.dev.dto.post;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import com.devin.dev.entity.post.PostStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDetailsDto {

    private Long postId;
    private Long userId;
    private String title;
    private String username;
    private String content;
    private PostStatus status;
    private Integer replyQuantity;
    private List<String> imagePaths;

    @QueryProjection
    public PostDetailsDto(Long postId, Long userId, String title, String username, String content, PostStatus status, Integer replyQuantity, List<String> imagePaths) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.username = username;
        this.content = content;
        this.status = status;
        this.replyQuantity = replyQuantity;
        this.imagePaths = imagePaths;
    }

    public PostDetailsDto(Post post) {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.username = post.getUser().getName();
        this.content = post.getContent();
        this.status = post.getStatus();
        this.replyQuantity = post.getReplies().size();
        this.imagePaths = post.getImages().stream().map(PostImage::getPath).collect(Collectors.toList());
    }
}
