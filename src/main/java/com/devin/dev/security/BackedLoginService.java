package com.devin.dev.security;

import com.devin.dev.repository.user.UserRepository;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.devin.dev.entity.user.User user = userRepository.findByEmailEquals(username).orElseThrow();

        return new User(user.getName(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getStatus().toString())));
    }
}
