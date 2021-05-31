package com.devin.dev.service;

import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    public DefaultResponse<?> signUp(UserSimpleDto userDto) {
        // 엔티티 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDto.getEmail());
        if(foundUser.isPresent()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.EXIST_USER_EMAIL);
        }

        // 비밀번호 암호화
        String password = userDto.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        userDto.setPassword(encodePassword);

        // 엔티티 생성 후 저장
        User user = new User(userDto);
        userRepository.save(user);

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.CREATED_USER);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<?> signIn(UserSimpleDto userDto) {
        // 엔티티 email 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDto.getEmail());
        if(foundUser.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_EXIST_EMAIL);
        }

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDto.getPassword(), foundUser.get().getPassword());
        if(!passwordCheck) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.INCORRECT_PASSWORD);
        }

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
    }
}
