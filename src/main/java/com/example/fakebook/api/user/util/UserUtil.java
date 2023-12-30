package com.example.fakebook.api.user.util;


import com.example.fakebook.api.auth.repository.RefreshTokenRepository;
import com.example.fakebook.api.user.entity.UserEntity;
import com.example.fakebook.api.user.repository.UserRepository;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.auth.token.dto.internal.RefreshTokenPayloadInternalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final TokenManager tokenManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public UserEntity getOwnerByRefreshToken(String refreshToken) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = tokenManager.decodeToken(refreshToken);

        refreshTokenRepository.findByRefreshTokenAndUserEntityIdThrowIfNull(
                refreshToken,
                refreshTokenPayloadInternalDto.getUserId()
        );
        return userRepository.findByIdThrowIfNull(refreshTokenPayloadInternalDto.getUserId());
    }

    public UserEntity getOwnerByAccessToken(String accessToken) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(accessToken);
        return userRepository.findByIdThrowIfNull(accessTokenPayloadInternalDto.getUserId());
    }
}