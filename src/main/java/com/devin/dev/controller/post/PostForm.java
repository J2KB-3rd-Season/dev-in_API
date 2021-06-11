package com.devin.dev.controller.post;

import lombok.Data;

import java.util.List;

@Data
public class PostForm {
    private String title;
    private String content;
    private List<String> post_images;
    private List<String> post_tags;
}
