package com.devin.dev;

import com.devin.dev.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SampleData {

    private final InitData initData;

    @PostConstruct
    public void init() {

    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitData {



    }
}
