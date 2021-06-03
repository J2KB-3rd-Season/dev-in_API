package com.devin.dev.sample;

import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return "hello.html";
    }

    @GetMapping("/hello/delete/{id}")
    public String deleteHello(@PathVariable("id") Long id) {
        helloService.deleteHello(id);
        return "redirect:/";
    }

    @PostMapping("/signUp")
    public String signUp(@Valid HelloForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }

        helloService.signUp(form.getData(), form.getPassword());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }

    @GetMapping("/signUp")
    public String getSignUpForm() {
        return "signupPage";
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }

}
