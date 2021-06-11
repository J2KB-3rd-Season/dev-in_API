package com.devin.dev.sample;

import com.devin.dev.model.DefaultResponse;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelloService {
    private final HelloJpaRepository helloJpaRepository;

    private final HelloRepository helloRepository;

    private final PasswordEncoder passwordEncoder;

    public void deleteHello(Long id) {
        Hello hello = helloJpaRepository.findById(id).get();
        helloJpaRepository.delete(hello);
    }

    public DefaultResponse<HelloDto> signUp(String data, String password) {
        Hello hello = Hello.builder()
                        .username(data)
                        .password(passwordEncoder.encode(password))
                        .role("admin")
                        .build();

        helloRepository.save(hello);

        HelloDto helloDto = new HelloDto(hello);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.CREATED_USER, helloDto);
    }

}
