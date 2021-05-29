package com.devin.dev.entity.reply;

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
public class ReplyLike extends Created {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeReply(Reply reply) {
        if(this.reply != null) {
            this.reply.getLikes().remove(this);
        } else {
            this.reply = reply;
            reply.getLikes().add(this);
        }
    }
}
