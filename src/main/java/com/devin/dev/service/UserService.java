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

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional
    public DefaultResponse<UserSimpleDto> signUp(UserSimpleDto userDto) {
        // 엔티티 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDto.getEmail());
        if (foundUser.isPresent()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.EXIST_USER_EMAIL);
        }

        // 비밀번호 암호화
        String password = userDto.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        userDto.setPassword(encodePassword);

        // 엔티티 생성 후 저장
        User user = new User(userDto);
        userRepository.save(user);

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.CREATED_USER, userDto);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<UserDetailsDto> signIn(UserSimpleDto userDto) {
        // DTO email 로 조회
        Optional<UserDetailsDto> userOptional = userRepository.findUserDetailsByEmail(userDto.getEmail());
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_EMAIL);
        }
        UserDetailsDto userDetailsDto = userOptional.get();

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDto.getPassword(), userDetailsDto.getPassword());
        if (!passwordCheck) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.INCORRECT_PASSWORD);
        }

        // 활성 유저 체크
        if (userDetailsDto.getStatus() != UserStatus.ACTIVE) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.INACTIVE_USER);
        }

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS);
    }

    @Transactional
    public DefaultResponse<UserDetailsDto> changeStatus(UserSimpleDto userDto, UserStatus userStatus) {
        // 엔티티 조회
        Optional<User> userOptional = userRepository.findByEmailEquals(userDto.getEmail());
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDto.getPassword(), user.getPassword());
        if (!passwordCheck) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.INCORRECT_PASSWORD);
        }

        // 상태 변경
        user.setStatus(userStatus);

        UserDetailsDto userDetailsDto = new UserDetailsDto(user);

        return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.CHANGE_USER_STATUS, userDetailsDto);
    }

}
