package com.devin.dev.service;

import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void signUpSucceed() {
        UserSimpleDto userDto = new UserSimpleDto("A", "a@b.com", "passA", "0001");
        DefaultResponse<?> response = userService.signUp(userDto);

        // 성공 response 객체 확인
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.CREATED_USER);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);

        // 저장된 유저 엔티티 확인
        Optional<User> foundUser = userRepository.findByEmailEquals("a@b.com");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getName()).isEqualTo("A");
        assertThat(foundUser.get().getEmail()).isEqualTo("a@b.com");
        assertThat(foundUser.get().getPassword()).isNotEqualTo("passA"); // password 암호화 확인
        assertThat(foundUser.get().getPhone_number()).isEqualTo("0001");

        // 암호화 password 비교
        boolean passwordCheck = passwordEncoder.matches("passA", foundUser.get().getPassword());
        assertThat(passwordCheck).isTrue();
    }

    @Test
    void signUpFailed() {
        // 이미 존재하는 유저
        UserSimpleDto existUserDto = new UserSimpleDto("A", "a@b.com", "passA", "0001");
        User existUser = new User(existUserDto);
        em.persist(existUser);
        em.flush();

        // 같은 이메일로 회원가입 시도
        UserSimpleDto userDto = new UserSimpleDto("B", "a@b.com", "passB", "0002");
        DefaultResponse<?> response = userService.signUp(userDto);

        // 실패 response 객체 확인
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.EXIST_USER_EMAIL);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);

        Optional<User> foundUser = userRepository.findByName("B");
        assertThat(foundUser.isEmpty()).isTrue();
    }

    @Test
    void signInSucceed() {
        // 유저 엔티티 생성 및 저장, 패스워드는 암호화 하여 저장
        UserSimpleDto userDto = new UserSimpleDto("A", "a@b.com",
                passwordEncoder.encode("passA"), "0001");
        User existUser = new User(userDto);
        em.persist(existUser);
        em.flush();

        // 암호화 하기 전 패스워드로 변경
        userDto.setPassword("passA");

        // 조회
        DefaultResponse<?> response = userService.signIn(userDto);

        System.out.println("response = " + response.getResponseMessage());
        // 성공 response 객체 확인
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.LOGIN_SUCCESS);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.OK);
    }

    @Test
    void signInFailedWrongEmail() {
        // 유저 엔티티 생성 및 저장, 패스워드는 암호화 하여 저장
        UserSimpleDto userDto = new UserSimpleDto("A", "a@b.com",
                passwordEncoder.encode("passA"), "0001");
        User existUser = new User(userDto);
        em.persist(existUser);
        em.flush();

        // 틀린 이메일
        userDto.setEmail("aa@b.com");
        userDto.setPassword("passA");

        // 조회
        DefaultResponse<?> response = userService.signIn(userDto);

        // 실패 response 객체 확인
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.NOT_FOUND_EMAIL);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);
    }

    @Test
    void signInFailedWrongPassword() {
        // 유저 엔티티 생성 및 저장, 패스워드는 암호화 하여 저장
        UserSimpleDto userDto = new UserSimpleDto("A", "a@b.com",
                passwordEncoder.encode("passA"), "0001");
        User existUser = new User(userDto);
        em.persist(existUser);
        em.flush();

        // 틀린 패스워드
        userDto.setPassword("paddmvjdj");

        // 조회
        DefaultResponse<?> response = userService.signIn(userDto);

        // 실패 response 객체 확인
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.INCORRECT_PASSWORD);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.BAD_REQUEST);
    }
}