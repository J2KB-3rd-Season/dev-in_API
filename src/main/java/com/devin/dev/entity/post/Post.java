package com.devin.dev.entity.post;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends ModifiedCreated {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "post")
    List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<PostImage> images = new ArrayList<>();

    String title;
    String content;

    @Enumerated
    PostState state;

}
