package com.devin.dev.entity.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ReplyImage {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    private String path;
    private int order;

    public ReplyImage(String path, int order) {
        this.path = path;
        this.order = order;
    }

    public static List<ReplyImage> createReplyImages(List<String> imagePaths) {

        List<ReplyImage> replyImages = new ArrayList<>();

        for (int i = 0; i < imagePaths.size(); i++) {
            ReplyImage replyImage = new ReplyImage(imagePaths.get(i), i);
            replyImages.add(replyImage);
        }

        return replyImages;
    }

}
