package com.example.boilerplate.api.token.entity;

import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshTokenEntity extends BaseEntity {
    private String refreshToken;
    @ManyToOne
    private UserEntity userEntity;
}
