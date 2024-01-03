package com.example.fakebook.api.auth.service;

import com.example.fakebook.api.auth.dto.request.TokenRenewRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignInRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignOutRequestDto;
import com.example.fakebook.api.auth.dto.response.AccessTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.RefreshTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.TokenResponseDto;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.api.member.util.MemberUtil;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.TokenInternalDto;
import com.example.fakebook.global.dto.response.SuccessResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;
    private final MemberUtil memberUtil;

    public SuccessResponseDto<TokenResponseDto> tokenSignIn(TokenSignInRequestDto tokenSignInRequestDto) {
        String encryptedSignPassword = CryptoUtil.encryptSha512(tokenSignInRequestDto.getSignPassword());

        Member member = memberRepository.findBySignIdAndSignPasswordAndDeletedAtIsNullThrowIfNull(
                tokenSignInRequestDto.getSignId(),
                encryptedSignPassword
        );

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                member.getId(),
                member.getRole()
        );

        member.addRefreshToken(tokenInternalDto.getRefreshTokenInternalDto().to());
        member.updateLastSignInAt();

        memberRepository.save(member);

        return new SuccessResponseDto<>(TokenResponseDto.from(tokenInternalDto));
    }

    public SuccessVoidResponseDto tokenSignOut(TokenSignOutRequestDto tokenSignOutRequestDto) {
        Member member = memberUtil.getOwnerByRefreshToken(tokenSignOutRequestDto.getRefreshToken());

        member.removeRefreshToken(tokenSignOutRequestDto.getRefreshToken());
        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }

    public SuccessResponseDto<AccessTokenResponseDto> tokenRenew(TokenRenewRequestDto tokenRenewRequestDto) {
        Member member = memberUtil.getOwnerByRefreshToken(tokenRenewRequestDto.getRefreshToken());

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                member.getId(),
                member.getRole()
        );

        return new SuccessResponseDto<>(AccessTokenResponseDto.from(tokenInternalDto.getAccessTokenInternalDto()));
    }

    public SuccessResponseDto<List<RefreshTokenResponseDto>> getRefreshTokenList(String authorizationHeader) {
        Member member = memberUtil.getOwnerByAccessToken(authorizationHeader);

        List<RefreshTokenResponseDto> refreshTokenResponseDtos = member.getRefreshTokens().stream()
                .map(RefreshTokenResponseDto::from)
                .toList();

        return new SuccessResponseDto<>(refreshTokenResponseDtos);
    }
}
