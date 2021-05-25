package com.devin.dev.dto.user;

import com.devin.dev.entity.user.UserStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserSimpleDto {

    private String name;
    private Long exp;
    private UserStatus status;

    @QueryProjection
    public UserSimpleDto(String name, Long exp, UserStatus status) {
        this.name = name;
        this.exp = exp;
        this.status = status;
    }
}
