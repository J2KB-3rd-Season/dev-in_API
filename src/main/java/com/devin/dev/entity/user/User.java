package com.devin.dev.entity.user;

import com.devin.dev.entity.base.Created;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.service.CustomerService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Created {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    Long id;

    @OneToMany(mappedBy = "user")
    private final List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<CustomerService> services = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<UserInterest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<UserSNS> sns = new ArrayList<>();

    private String name;
    private String email;
    private String password;
    private String phone_number;
    private Long exp;
    private String profile;

    @Enumerated
    private UserStatus status;

    private String sns_type;

    public User(String name, String email, String password, String phone_number, UserStatus status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.status = status;
        this.exp = 0L;
    }

    public void addExp(ExpIncreaseType type) {
        this.exp += type.getIncrease();
    }

    public void subExp(ExpIncreaseType type) {
        this.exp += type.getIncrease();
    }

    public enum ExpIncreaseType {
        POST(1),
        REPLY(3),
        POST_LIKE_WRITER(1),
        POST_LIKE(1),
        REPLY_LIKE_WRITER(1),
        REPLY_LIKE(1),
        REPLY_SELECT(5),
        REPLY_SELECT_WRITER(10);

        private final int increase;

        ExpIncreaseType(int increase) {
            this.increase = increase;
        }

        public int getIncrease() {
            return increase;
        }
    }

}
