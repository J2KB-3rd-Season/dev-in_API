package com.devin.dev.entity.post;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.reply.Reply;
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

    @Setter
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

    public static Post createPostWithImages(User user, String title, String content, List<PostTag> tags, List<PostImage> images) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setContent(content);
        post.setStatus(PostStatus.VIEWABLE);

        setPostTags(post, tags);
        setPostImages(post, images);

        return post;
    }

    private static void setPostTag(Post post, PostTag tag) {
        post.tags.add(tag);
        tag.setPost(post);
    }

    private static void setPostTags(Post post, List<PostTag> tags) {
        for (PostTag tag : tags) {
            setPostTag(post, tag);
        }
    }

    public void setPostTags(List<PostTag> tags) {
        setPostTags(this, tags);
    }

    private static void setPostImage(Post post, PostImage image) {
        post.images.add(image);
        image.setPost(post);
    }

    private static void setPostImages(Post post, List<PostImage> images) {
        post.images.clear();
        for (PostImage image : images) {
            setPostImage(post, image);
        }
    }

    public void setPostImages(List<PostImage> postImages) {
        setPostImages(this, postImages);
    }
}
