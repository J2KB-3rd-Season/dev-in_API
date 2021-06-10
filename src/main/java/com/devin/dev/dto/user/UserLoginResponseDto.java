package com.devin.dev.dto.user;

import com.devin.dev.entity.user.User;
import com.devin.dev.security.JwtAuthToken;
import lombok.Data;

@Data
public class UserLoginResponseDto {
    private Long userId;
    private String userProfile;
    private String userName;
    private String token;

    public UserLoginResponseDto(User user, JwtAuthToken token) {
        this.userId = user.getId();
        this.userProfile = user.getProfile();
        this.userName = user.getName();
        this.token = token.getToken();
    }
}
