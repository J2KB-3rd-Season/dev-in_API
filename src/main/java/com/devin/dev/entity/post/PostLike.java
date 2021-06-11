package com.devin.dev.entity.post;

import com.devin.dev.entity.base.Created;
import com.devin.dev.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostLike extends Created {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changePost(Post post) {
        if(this.post != null) {
            this.post.getLikes().remove(this);
        } else {
            this.post = post;
            post.getLikes().add(this);
        }
    }
}
