package com.example.fakebook.api.member.entity;

import com.example.fakebook.api.auth.entity.RefreshToken;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.enums.Role;
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
})
public class Member extends Base {
    private String signId;
    private String signPassword;
    private LocalDateTime lastSignInAt = LocalDateTime.now();

    private String email;
    private String phoneNumber;

    private LocalDateTime blacklistedAt;

    private Role role = Role.USER;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setMember(this);
    }

    public void removeRefreshToken(String token) {
        refreshTokens.removeIf((entity) -> token.equals(entity.getToken()));
    }

    public void applyBlacklist() {
        blacklistedAt = LocalDateTime.now();
    }

    public void revokeBlacklist() {
        blacklistedAt = null;
    }

    public void updateLastSignInAt() {
        lastSignInAt = LocalDateTime.now();
    }
}
