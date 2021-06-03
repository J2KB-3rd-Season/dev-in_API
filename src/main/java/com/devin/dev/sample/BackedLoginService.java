package com.devin.dev.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BackedLoginService implements UserDetailsService {

    private final HelloRepository helloRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hello hello = helloRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return new User(hello.getUsername(), hello.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(hello.getRole())));
    }
}
