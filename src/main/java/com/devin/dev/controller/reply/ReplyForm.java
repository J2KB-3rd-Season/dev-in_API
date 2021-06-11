package com.devin.dev.controller.reply;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ReplyForm {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String content;

    private List<String> reply_image;
}