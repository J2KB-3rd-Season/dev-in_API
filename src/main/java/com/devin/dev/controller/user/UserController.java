package com.devin.dev.controller.user;

import com.devin.dev.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;



}
