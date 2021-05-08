package com.devin.dev.sample;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelloDTO {
    private Long id;
    private String data;
}
