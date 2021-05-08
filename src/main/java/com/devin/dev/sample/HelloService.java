package com.devin.dev.sample;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    HelloRepository helloRepository;

    public void deleteHello(Long id) {
        Hello hello = helloRepository.findById(id).get();
        helloRepository.delete(hello);
    }
}
