package com.devin.dev.entity.post;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends ModifiedCreated {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private final List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostRecommend> postRecommends = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostImage> images = new ArrayList<>();

    @NotNull
    private  String title;

    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    private PostState state;

    public Post(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
