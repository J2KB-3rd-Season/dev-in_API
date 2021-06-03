package com.devin.dev.sample;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hello {

    @Id
    @GeneratedValue
    Long id;

    private String password;
    private String username;
    private String role;
    private int price;

    public Hello(String username) {
        this.username = username;
    }

    public Hello(String username, int price) {
        this.username = username;
        this.price = price;
    }

    @Builder
    public Hello(String password, String username, String role) {
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
