package com.stejsoftware.zengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.stejsoftware.zengine", "io.socket.spring" })
@SpringBootApplication
public class ZEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZEngineApplication.class, args);
    }
}
