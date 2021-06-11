package com.devin.dev.service;

import com.devin.dev.controller.subject.SubjectForm;
import com.devin.dev.entity.post.Subject;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostTagRepository;
import com.devin.dev.repository.subject.SubjectRepository;
import com.devin.dev.repository.user.UserInterestRepository;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.security.JwtAuthTokenProvider;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;
    private final JwtAuthTokenProvider tokenProvider;

    @Transactional
    public DefaultResponse<?> addSubject(SubjectForm form, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        if (user.getStatus() != UserStatus.ADMIN) {
            return new DefaultResponse<>(StatusCode.CONDITION_FAIL, ResponseMessage.NO_AUTHORITY);
        }

        List<Subject> subjects = form.getSubjects().stream().map(Subject::new).collect(Collectors.toList());
        subjectRepository.saveAll(subjects);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.CREATED_SUBJECT);
    }

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
