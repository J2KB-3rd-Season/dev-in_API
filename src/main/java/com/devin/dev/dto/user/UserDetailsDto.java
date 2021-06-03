package com.devin.dev.dto.user;

import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class UserDetailsDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone_number;
    private Long exp;
    private String profile;
    private UserStatus status;
    private String sns_type;
    private String description;

    @QueryProjection
    public UserDetailsDto(Long id, String name, String email, String password, String phone_number, Long exp, String profile, UserStatus status, String sns_type, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.exp = exp;
        this.profile = profile;
        this.status = status;
        this.sns_type = sns_type;
        this.description = description;
    }

    public UserDetailsDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone_number = user.getPhone_number();
        this.exp = user.getExp();
        this.profile = user.getProfile();
        this.status = user.getStatus();
        this.sns_type = user.getSns_type();
        this.description = user.getDescription();
    }
}
