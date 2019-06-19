package com.example.geece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.example.geece.*"})
@SpringBootApplication
public class GeeceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeeceApplication.class, args);
    }
}

