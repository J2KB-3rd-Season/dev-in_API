package com.devin.dev.controller.Auth;

import com.devin.dev.dto.user.UserLoginRequestDto;
import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/user/login")
    public DefaultResponse<?> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return userService.login(userLoginRequestDto);
    }

    @PostMapping("/user/join")
    public DefaultResponse<?> join(@RequestBody UserSimpleDto userSimpleDto) {
        return userService.join(userSimpleDto);
    }
}
