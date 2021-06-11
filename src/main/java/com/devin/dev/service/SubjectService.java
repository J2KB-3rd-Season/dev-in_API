package com.devin.dev.service;

import com.devin.dev.entity.post.Subject;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.subject.SubjectRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public DefaultResponse<?> addSubject(String name) {
        Optional<Subject> subjectOptional = subjectRepository.findByName(name);
        if (subjectOptional.isPresent()) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.EXIST_SUBJECT);
        }
        Subject subject = new Subject(name);

        subjectRepository.save(subject);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.CREATED_SUBJECT);
    }

    @Transactional
    public DefaultResponse<?> removeSubject(String name) {
        Optional<Subject> subjectOptional = subjectRepository.findByName(name);
        if (subjectOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_SUBJECT);
        }
        subjectRepository.delete(subjectOptional.get());

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.DELETE_SUBJECT);
    }
}
