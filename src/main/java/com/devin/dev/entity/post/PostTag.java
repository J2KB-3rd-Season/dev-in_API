package com.devin.dev.entity.post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostTag {

    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject tag;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostTag(Subject tag) {
        this.tag = tag;
    }

    public static List<PostTag> createPostTags(List<Subject> tags) {
        List<PostTag> postTags = new ArrayList<>();

        for (Subject tag : tags) {
            PostTag postTag = new PostTag(tag);
            postTags.add(postTag);
        }

        return postTags;
    }
}
