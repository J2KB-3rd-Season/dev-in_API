package com.devin.dev.entity.reply;

import com.devin.dev.entity.base.ModifiedCreated;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.user.User;
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
public class Reply extends ModifiedCreated {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply")
    private final List<ReplyLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "reply")
    private final List<ReplyImage> images = new ArrayList<>();

    @Setter
    private String content;

    @Setter
    @Enumerated(EnumType.STRING)
    private ReplyStatus status;

    public static Reply createReply(Post post, User user, String content) {
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(content);
        reply.setStatus(ReplyStatus.NOT_CHOSEN);

        return reply;
    }

    public static Reply createReplyWithImages(Post post, User user, List<ReplyImage> images, String content) {
        Reply reply = new Reply();
        reply.setPost(post);
        reply.setUser(user);
        reply.setContent(content);
        reply.setStatus(ReplyStatus.NOT_CHOSEN);

        setReplyImages(reply, images);

        return reply;
    }

    public void setPost(Post post) {
        if(this.post != null) {
            this.post.getReplies().remove(this);
        }
        this.post = post;
        post.getReplies().add(this);
    }

    private static void setReplyImage(Reply reply, ReplyImage replyImage) {
        replyImage.setReply(reply);
    }

    private static void setReplyImages(Reply reply, List<ReplyImage> images) {
        reply.images.clear();
        for (ReplyImage image : images) {
            setReplyImage(reply, image);
        }
    }

    public void setReplyImages(List<ReplyImage> images) {
        setReplyImages(this, images);
    }

    public ReplyLike like(User user, ReplyLike replyLike) {
        replyLike.changeReply(this);
        replyLike.setUser(user);
        return replyLike;
    }

}
