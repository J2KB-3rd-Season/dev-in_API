package com.devin.dev.controller.post;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ReplyUpdateForm {
    @NotEmpty
    private String content;

    private List<String> reply_image;
}
