package com.devin.dev.entity.reply;

import javax.persistence.*;

@Entity
public class ReplyImage {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    Reply reply;

    String url;
    int order;
}
