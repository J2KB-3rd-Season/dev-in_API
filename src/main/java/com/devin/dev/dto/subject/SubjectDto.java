package com.devin.dev.dto.subject;

import com.devin.dev.entity.post.Subject;
import lombok.Data;

@Data
public class SubjectDto {

    private Long tag_id;
    private String tag_name;

    public SubjectDto(Subject subject) {
        this.tag_id = subject.getId();
        this.tag_name = subject.getName();
    }
}
