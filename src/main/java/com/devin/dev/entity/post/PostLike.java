package com.devin.dev.entity.post;

import com.devin.dev.entity.base.Created;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLike extends Created {

    @Id
    @GeneratedValue
    @Column(name = "post_like_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

}
