package com.devin.dev.security;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtAuthToken {
    private String token;
}
