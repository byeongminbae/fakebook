package com.example.fakebook.api.auth.entity;

import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.entity.Base;
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
public class RefreshToken extends Base {
    private String token;
    private LocalDateTime expiredAt;

    @ManyToOne
    private Member member;
}
