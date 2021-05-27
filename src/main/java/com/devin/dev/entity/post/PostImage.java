package com.devin.dev.entity.post;

import com.devin.dev.entity.reply.ReplyImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Setter(AccessLevel.PACKAGE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String path;

    private long file_size;

    public static List<PostImage> createPostImages(List<String> imagePaths) {
        List<PostImage> postImages = new ArrayList<>();

        for (String imagePath : imagePaths) {
            PostImage postImage = new PostImage(imagePath);
            postImages.add(postImage);
        }

        return postImages;
    }

    public PostImage(String path) {
        this.path = path;
    }
}
