package com.devin.dev.controller.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostUpdateForm {
    private String title;
    private String content;
    private List<String> post_images;
    private List<String> post_tags;

    public PostUpdateForm(Long id, String title, String content, List<String> post_images, List<String> post_tags) {
        this.title = title;
        this.content = content;
        this.post_images = post_images;
        this.post_tags = post_tags;
    }
}
