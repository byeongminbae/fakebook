package com.example.fakebook.api.member.util;


import com.example.fakebook.api.auth.repository.RefreshTokenRepository;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.RefreshTokenPayloadInternalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {
    private final TokenManager tokenManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public Member getOwnerByRefreshToken(String refreshToken) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = tokenManager.decodeToken(refreshToken);
        refreshTokenRepository.findByTokenThrowIfNull(refreshToken);
        return memberRepository.findByIdThrowIfNull(refreshTokenPayloadInternalDto.getMemberId());
    }

    public Member getOwnerByAccessToken(String accessToken) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(accessToken);
        return memberRepository.findByIdThrowIfNull(accessTokenPayloadInternalDto.getMemberId());
    }
}