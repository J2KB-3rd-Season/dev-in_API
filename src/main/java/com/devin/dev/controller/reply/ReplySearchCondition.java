package com.devin.dev.controller.reply;

import lombok.Data;

@Data
public class ReplySearchCondition {
    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}
