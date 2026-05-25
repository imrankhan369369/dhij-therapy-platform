package com.dhij.app.com.dhij.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync

@SpringBootApplication
public class DhijApplication {

    public static void main(String[] args) {
        SpringApplication.run(DhijApplication.class, args);
    }

}