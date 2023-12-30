package com.example.fakebook.api.auth.service;

import com.example.fakebook.api.auth.dto.request.TokenRenewRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignInRequestDto;
import com.example.fakebook.api.auth.dto.request.TokenSignOutRequestDto;
import com.example.fakebook.api.auth.dto.response.AccessTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.RefreshTokenResponseDto;
import com.example.fakebook.api.auth.dto.response.TokenResponseDto;
import com.example.fakebook.api.user.entity.UserEntity;
import com.example.fakebook.api.user.repository.UserRepository;
import com.example.fakebook.api.user.util.UserUtil;
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
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final UserUtil userUtil;

    public SuccessResponseDto<TokenResponseDto> tokenSignIn(TokenSignInRequestDto tokenSignInRequestDto) {
        String encryptedSignPassword = CryptoUtil.encryptSha512(tokenSignInRequestDto.getSignPassword());

        UserEntity userEntity = userRepository.findBySignIdAndSignPasswordAndIsDeletedThrowIfNull(
                tokenSignInRequestDto.getSignId(),
                encryptedSignPassword,
                false
        );
        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                userEntity.getId(),
                userEntity.getUserRole()
        );

        userEntity.addRefreshTokenEntity(tokenInternalDto.getRefreshTokenInternalDto().to());
        userEntity.updateLastSignInAt();

        userRepository.save(userEntity);

        return new SuccessResponseDto<>(TokenResponseDto.from(tokenInternalDto));
    }

    public SuccessVoidResponseDto tokenSignOut(TokenSignOutRequestDto tokenSignOutRequestDto) {
        UserEntity userEntity = userUtil.getOwnerByRefreshToken(tokenSignOutRequestDto.getRefreshToken());

        userEntity.removeRefreshTokenEntity(tokenSignOutRequestDto.getRefreshToken());
        userRepository.save(userEntity);

        return new SuccessVoidResponseDto();
    }

    public SuccessResponseDto<AccessTokenResponseDto> tokenRenew(TokenRenewRequestDto tokenRenewRequestDto) {
        UserEntity userEntity = userUtil.getOwnerByRefreshToken(tokenRenewRequestDto.getRefreshToken());

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                userEntity.getId(),
                userEntity.getUserRole()
        );

        return new SuccessResponseDto<>(AccessTokenResponseDto.from(tokenInternalDto.getAccessTokenInternalDto()));
    }

    public SuccessResponseDto<List<RefreshTokenResponseDto>> getRefreshTokenList(String authorizationHeader) {
        UserEntity userEntity = userUtil.getOwnerByAccessToken(authorizationHeader);

        List<RefreshTokenResponseDto> refreshTokenResponseDtos = userEntity.getRefreshTokenEntities().stream()
                .map(RefreshTokenResponseDto::from)
                .toList();

        return new SuccessResponseDto<>(refreshTokenResponseDtos);
    }
}
