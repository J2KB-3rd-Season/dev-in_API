package com.devin.dev.entity.reply;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply extends ModifiedCreated {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

    @OneToMany(mappedBy = "reply")
    List<ReplyLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "reply")
    List<ReplyImage> images = new ArrayList<>();

    String content;

    @Enumerated
    ReplyState state;

}
