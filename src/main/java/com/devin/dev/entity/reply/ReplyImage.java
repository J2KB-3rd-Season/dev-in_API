package com.devin.dev.entity.reply;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public ReplyImage(String path) {
        this.path = path;
    }

    public static List<ReplyImage> createReplyImages(List<String> imagePaths) {

        List<ReplyImage> replyImages = new ArrayList<>();

        for (String imagePath : imagePaths) {
            ReplyImage replyImage = new ReplyImage(imagePath);
            replyImages.add(replyImage);
        }

        return replyImages;
    }

}
