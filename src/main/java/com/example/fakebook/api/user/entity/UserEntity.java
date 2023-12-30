package com.example.fakebook.api.user.entity;

import com.example.fakebook.api.auth.entity.RefreshTokenEntity;
import com.example.fakebook.global.entity.BaseEntity;
import com.example.fakebook.global.entity.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "index_sign_id", columnList = "signId", unique = true),
        @Index(name = "index_sign_id_and_sign_password", columnList = "signId, signPassword"),
        @Index(name = "index_email", columnList = "email"),
        @Index(name = "index_phone_number", columnList = "phoneNumber"),
        @Index(name = "index_is_blacklisted", columnList = "isBlacklisted")
})
public class UserEntity extends BaseEntity {
    private String signId;
    private String signPassword;
    private LocalDateTime lastSignInAt = LocalDateTime.now();

    private String email;
    private String phoneNumber;

    private Boolean isBlacklisted = false;
    private LocalDateTime blacklistedAt;

    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;

    private UserRole userRole = UserRole.USER;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokenEntities;

    public void addRefreshTokenEntity(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntities.add(refreshTokenEntity);
        refreshTokenEntity.setUserEntity(this);
    }

    public void removeRefreshTokenEntity(String refreshToken) {
        refreshTokenEntities.removeIf((entity) -> refreshToken.equals(entity.getRefreshToken()));
    }

    public void applyBlacklist() {
        isBlacklisted = true;
        blacklistedAt = LocalDateTime.now();
    }

    public void revokeBlacklist() {
        isBlacklisted = false;
        blacklistedAt = null;
    }

    public void deleteUser() {
        isDeleted = true;
        deletedAt = LocalDateTime.now();
    }

    public void updateLastSignInAt() {
        lastSignInAt = LocalDateTime.now();
    }
}
