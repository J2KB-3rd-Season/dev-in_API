package com.devin.dev.dto.user;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    String userEmail;
    String userPassword;
}
