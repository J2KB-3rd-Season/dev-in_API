package com.devin.dev.dto.user;

import com.devin.dev.entity.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserSimpleDto {

    private String name;
    private String email;
    private String password;
    private String phone_number;

    @QueryProjection
    public UserSimpleDto(String name, String email, String password, String phone_number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
    }
}
