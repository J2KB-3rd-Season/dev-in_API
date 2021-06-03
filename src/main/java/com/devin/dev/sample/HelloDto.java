package com.devin.dev.sample;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelloDto {
    private Long id;
    private String data;
    private String password;

    public HelloDto(Hello hello) {
        this.id = hello.getId();
        this.data = hello.getUsername();
        this.password = hello.getPassword();
    }
}
