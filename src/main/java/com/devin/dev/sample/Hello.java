package com.devin.dev.sample;

import lombok.AccessLevel;
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

    private String data;
    private int price;

    public Hello(String data) {
        this.data = data;
    }

    public Hello(String data, int price) {
        this.data = data;
        this.price = price;
    }
}
