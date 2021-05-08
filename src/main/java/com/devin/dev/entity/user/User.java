package com.devin.dev.entity.user;

import com.devin.dev.entity.base.Created;
import com.devin.dev.entity.post.Post;
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

    public User(String name, String email, String password, String phone_number, UserStatus status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.status = status;
    }

    @OneToMany(mappedBy = "user")
    List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<CustomerService> services = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserInterest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserSNS> sns = new ArrayList<>();

    String name;
    String email;
    String password;
    String phone_number;
    Long exp;
    String profile;

    @Enumerated
    UserStatus status;

    String sns_type;

}
