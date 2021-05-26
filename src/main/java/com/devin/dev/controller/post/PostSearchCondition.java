package com.devin.dev.controller.post;

import lombok.Data;

import java.util.List;

@Data
public class PostSearchCondition {

    private String username;
    private String title;
    private List<String> tags;
}
