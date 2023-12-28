package com.example.boilerplate.api.auth.entity;

import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshTokenEntity extends BaseEntity {
    private String refreshToken;
    private LocalDateTime expiredAt;

    @ManyToOne
    private UserEntity userEntity;
}
