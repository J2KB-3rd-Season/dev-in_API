package com.devin.dev.dto;

import com.devin.dev.entity.user.UserStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone_number;
    private Long exp;
    private String profile;

    private UserStatus status;

    private String sns_type;
}
