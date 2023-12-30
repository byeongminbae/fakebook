package com.example.fakebook.api.auth.entity;

import com.example.fakebook.api.user.entity.UserEntity;
import com.example.fakebook.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
