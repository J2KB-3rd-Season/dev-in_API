package com.devin.dev.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/delete/{id}")
    public String deleteHello(@PathVariable("id") Long id) {
        helloService.deleteHello(id);
        return "redirect:/";
    }

}
