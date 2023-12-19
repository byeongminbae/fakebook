package com.example.boilerplate.api.user.entity;

import com.example.boilerplate.api.token.entity.RefreshTokenEntity;
import com.example.boilerplate.global.entity.BaseEntity;
import com.example.boilerplate.global.entity.UserRole;
import com.example.boilerplate.global.entity.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    private String nickname;
    private String email;
    private UserRole userRole;
    private UserStatus userStatus;

    // 영속성 전이. UserEntity 를 저장하기 전에 RefreshTokenEntity 를 먼저 저장하는 옵션
    @OneToMany(cascade = CascadeType.ALL)
    private List<RefreshTokenEntity> refreshTokenEntities;

    public void appendRefreshTokenEntity(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntities.add(refreshTokenEntity);
        refreshTokenEntity.setUserEntity(this);
    }
}
