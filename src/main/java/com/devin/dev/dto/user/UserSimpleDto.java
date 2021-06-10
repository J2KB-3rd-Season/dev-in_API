package com.devin.dev.dto.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserSimpleDto {
    private String userName;
    @NotEmpty(message = "email은 필수 입니다")
    private String userEmail;
    @NotEmpty(message = "password는 필수 입니다")
    private String userPassword;
    private String userPhoneNumber;

    @QueryProjection
    public UserSimpleDto(String userName, String userEmail, String userPassword, String userPhoneNumber) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
    }
}
