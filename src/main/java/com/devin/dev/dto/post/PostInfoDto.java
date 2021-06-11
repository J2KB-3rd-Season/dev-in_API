package com.devin.dev.dto.post;

import com.devin.dev.entity.post.PostStatus;
import com.devin.dev.entity.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostInfoDto {
    private Long postId;
    private String title;
    private List<String> tags;
    private boolean statusAccept;
    private boolean statusLike;
    private Integer replyNum;
    private Integer likeNum;
    private LocalDateTime createdDate;
    private Long userId;
    private String username;
    private Long userExp;
    private String userProfile;

    @QueryProjection
    public PostInfoDto(Long postId, String title, List<String> tags, boolean statusAccept, boolean statusLike, Integer replyNum, Integer likeNum, LocalDateTime createdDate, Long userId, String username, Long userExp, String userProfile) {
        this.postId = postId;
        this.title = title;
        this.tags = tags;
        this.statusAccept = statusAccept;
        this.statusLike = statusLike;
        this.replyNum = replyNum;
        this.likeNum = likeNum;
        this.createdDate = createdDate;
        this.userId = userId;
        this.username = username;
        this.userExp = userExp;
        this.userProfile = userProfile;
    }

    public PostInfoDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.tags = post.getPostTags();
        this.statusAccept = post.getStatus() == PostStatus.SELECTED;
        this.statusLike = post.getLikes().size() > 0;
        this.replyNum = post.getReplies().size();
        this.likeNum = post.getLikes().size();
        this.createdDate = post.getCreatedDate();
        this.userId = post.getUser().getId();
        this.username = post.getUser().getName();
        this.userExp = post.getUser().getExp();
        this.userProfile = post.getUser().getProfile();
    }
}
