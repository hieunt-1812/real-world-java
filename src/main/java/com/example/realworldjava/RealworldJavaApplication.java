package com.example.realworldjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RealworldJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealworldJavaApplication.class, args);
    }

}
