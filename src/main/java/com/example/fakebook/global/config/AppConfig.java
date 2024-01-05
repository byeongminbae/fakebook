package com.example.fakebook.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Configuration
public class AppConfig {
    @Bean
    @Scope("prototype")
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }
}
