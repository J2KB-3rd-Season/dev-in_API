package com.devin.dev.service;

import com.devin.dev.dto.user.UserDetailsDto;
import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
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

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    @Transactional
    public DefaultResponse<?> signUp(UserDetailsDto userDetailsDto) {
        // 엔티티 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDetailsDto.getEmail());
        if(foundUser.isPresent()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.EXIST_USER_MAIL);
        }

        // 비밀번호 암호화
        String password = userDetailsDto.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        userDetailsDto.setPassword(encodePassword);

        // 엔티티 생성 후 저장
        User user = new User(userDetailsDto);
        userRepository.save(user);

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.CREATED_USER);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<?> signIn(UserDetailsDto userDetailsDto) {
        // 엔티티 email 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDetailsDto.getEmail());
        if(foundUser.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
        }

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDetailsDto.getPassword(), foundUser.get().getPassword());
        if(!passwordCheck) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
        }

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
    }
}
