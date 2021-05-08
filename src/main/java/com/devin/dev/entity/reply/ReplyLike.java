package com.devin.dev.entity.reply;

import com.devin.dev.entity.base.Created;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyLike extends Created {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    Reply reply;
}
