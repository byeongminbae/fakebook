package com.example.fakebook.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final EntityManager entityManager;

    @Bean
    @Scope("prototype")
    public LocalDateTime localDateTime(){
        return LocalDateTime.now();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
