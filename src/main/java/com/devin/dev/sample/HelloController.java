package com.devin.dev.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return "hello.html";
    }

    @GetMapping("/hello/delete/{id}")
    public String deleteHello(@PathVariable("id") Long id) {
        helloService.deleteHello(id);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }

}
