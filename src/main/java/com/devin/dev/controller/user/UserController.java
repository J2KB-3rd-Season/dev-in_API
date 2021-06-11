package com.devin.dev.controller.user;

import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.sample.HelloForm;
import com.devin.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

//    private final UserService userService;
//
//    @GetMapping("/login")
//    public String getLoginForm() {
//        return "loginPage";
//    }
//
//    @GetMapping("/signUp")
//    public String getSignUpForm() {
//        return "signupPage";
//    }
//
//    @PostMapping("/signUp")
//    public String signUp(@Valid UserSimpleDto form, BindingResult result) {
//
//        System.out.println("form = " + form);
//
//        if (result.hasErrors()) {
//            return "redirect:/";
//        }
//
//
//        userService.signUp(form);
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/")
//    public String getIndex(Model model) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        model.addAttribute("username", username);
//
//        return "index";
//    }

}
