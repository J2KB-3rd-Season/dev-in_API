package com.devin.dev.sample;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class HelloForm {
    @NotEmpty(message = "Data 필수 입니다")
    private String data;
    private String password;
}
