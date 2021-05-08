package com.devin.dev.entity.post;

import javax.persistence.*;

@Entity
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    String url;
    int order;
}
