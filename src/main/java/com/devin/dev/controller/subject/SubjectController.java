package com.devin.dev.controller.subject;

import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/tags")
    public DefaultResponse<?> addSubject(@RequestBody SubjectForm form, HttpServletRequest request) {
        return subjectService.addSubject(form, request);
    }

    @DeleteMapping("/tags")
    public DefaultResponse<?> deleteSubject(@RequestBody SubjectForm form, HttpServletRequest request) {
        return subjectService.removeSubject(form, request);
    }
}
