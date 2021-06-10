package com.devin.dev.service;

import com.devin.dev.dto.user.UserDetailsDto;
import com.devin.dev.dto.user.UserLoginRequestDto;
import com.devin.dev.dto.user.UserLoginResponseDto;
import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.security.JwtAuthToken;
import com.devin.dev.security.JwtAuthTokenProvider;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public DefaultResponse<UserDetailsDto> signUp(UserSimpleDto userDto) {
        // 엔티티 조회
        Optional<User> foundUser = userRepository.findByEmailEquals(userDto.getUserEmail());
        if (foundUser.isPresent()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.EXIST_USER_EMAIL);
        }

        // 비밀번호 암호화
        String password = userDto.getUserPassword();
        String encodePassword = passwordEncoder.encode(password);
        userDto.setUserPassword(encodePassword);

        // 엔티티 생성 후 저장
        User user = new User(userDto);
        userRepository.save(user);

        UserDetailsDto userDetailsDto = new UserDetailsDto(user);

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.CREATED_USER, userDetailsDto);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<UserDetailsDto> signIn(UserSimpleDto userDto) {
        // DTO email 로 조회
        Optional<UserDetailsDto> userOptional = userRepository.findUserDetailsByEmail(userDto.getUserEmail());
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_EMAIL);
        }
        UserDetailsDto userDetailsDto = userOptional.get();

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDto.getUserPassword(), userDetailsDto.getPassword());
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
        Optional<User> userOptional = userRepository.findByEmailEquals(userDto.getUserEmail());
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();

        // 비밀번호 체크
        boolean passwordCheck = passwordEncoder.matches(userDto.getUserPassword(), user.getPassword());
        if (!passwordCheck) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.INCORRECT_PASSWORD);
        }

        // 상태 변경
        user.setStatus(userStatus);

        UserDetailsDto userDetailsDto = new UserDetailsDto(user);

        return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.CHANGE_USER_STATUS, userDetailsDto);
    }

    @Transactional
    public User findUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isPresent()) {
            return findUser.get();
        }
        return null;
    }

    @Transactional
    public DefaultResponse<?> join(UserSimpleDto userSimpleDto) {
        userSimpleDto.setUserPassword(passwordEncoder.encode(userSimpleDto.getUserPassword()));
        User user = userRepository.save(new User(userSimpleDto));
        return new DefaultResponse<>(StatusCode.CREATED, ResponseMessage.CREATED_USER, user.getId());
    }

    @Transactional
    public DefaultResponse<?> login(UserLoginRequestDto userLoginRequestDto) {
        String email = userLoginRequestDto.getUserEmail();
        String password = userLoginRequestDto.getUserPassword();
        User user = userRepository.findByEmailEquals(email)
                    .orElseThrow(() -> new RuntimeException());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Password not matched");
        }
        JwtAuthToken token = jwtAuthTokenProvider.publishToken(user.getId());
        UserLoginResponseDto responseDto = new UserLoginResponseDto(user, token);
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, responseDto);
    }
}
