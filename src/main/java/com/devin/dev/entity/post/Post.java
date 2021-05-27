package com.devin.dev.entity.post;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.reply.ReplyImage;
import com.devin.dev.entity.reply.ReplyStatus;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter(AccessLevel.PRIVATE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private final List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<PostImage> images = new ArrayList<>();

    @NotNull
    private  String title;

    @Setter
    @NotNull
    private String content;

    @Setter
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    public Post(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public static Post createPostWithImages(User user, List<PostImage> images, String content) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setStatus(PostStatus.VIEWABLE);

        setPostImages(images, post);

        return post;
    }

    public static void setPostImages(List<PostImage> images, Post post) {
        post.images.clear();
        for (PostImage image : images) {
            setReplyImage(post, image);
        }
    }

    private static void setReplyImage(Post post, PostImage image) {
        post.images.add(image);
        image.setPost(post);
    }

    public void setTags(List<PostTag> tags) {
        this.images.clear();
        for (PostTag tag : tags) {
            setTag(tag);
        }
    }

    public void setTag(PostTag tag) {
        this.tags.add(tag);
        tag.setPost(this);
    }

    public void setImages(List<PostImage> newPostImages) {
        this.images.clear();
        for (PostImage image : newPostImages) {
            setReplyImage(this, image);
        }
    }
}
