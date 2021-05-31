package com.devin.dev.entity.user;

import com.devin.dev.dto.user.UserDetailsDto;
import com.devin.dev.entity.base.Created;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.reply.Reply;
import com.devin.dev.entity.service.CustomerService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long id;

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
    private final List<UserSns> sns = new ArrayList<>();

    @Setter
    private String name;
    @Setter
    private String email;

    private String password;
    private Long exp;

    @Setter
    private String phone_number;
    @Setter
    private String profile;

    @Enumerated(EnumType.STRING)
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

    public User(UserDetailsDto userDto) {
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.phone_number = userDto.getPhone_number();
        this.status = UserStatus.ACTIVE;
        this.exp = 0L;
    }

    private static void setUserInterest(User user, UserInterest interest) {
        user.interests.add(interest);
        interest.setUser(user);
    }

    private static void setUserInterests(User user, List<UserInterest> interests) {
        user.interests.clear();
        for (UserInterest interest : interests) {
            setUserInterest(user, interest);
        }
    }

    public void setUserInterests(List<UserInterest> interests) {
        setUserInterests(this, interests);
    }

    public void changeExp(ExpChangeType type) {
        this.exp += type.getChange();
    }

    public enum ExpChangeType {
        CREATE_POST(1),
        CREATE_REPLY(3),
        POST_BE_LIKED(1),
        POST_LIKE(1),
        REPLY_BE_LIKED(1),
        REPLY_LIKE(1),
        REPLY_SELECT(5),
        REPLY_BE_SELECTED(10),
        DELETE_POST(-1),
        DELETE_REPLY(-3),
        POST_NOT_BE_LIKED(-1),
        POST_CANCEL_LIKE(-1),
        REPLY_NOT_BE_LIKED(-1),
        REPLY_CANCEL_LIKE(-1),
        REPLY_CANCEL_SELECT(-5),
        REPLY_NOT_BE_SELECTED(-10);

        private final int change;

        ExpChangeType(int change) {
            this.change = change;
        }

        public int getChange() {
            return change;
        }
    }
}
