package com.example.boilerplate.api.token.service;

import com.example.boilerplate.api.token.dto.request.CreateTokenRequestDto;
import com.example.boilerplate.api.token.dto.request.RenewTokenRequestDto;
import com.example.boilerplate.api.token.dto.response.AccessTokenResponseDto;
import com.example.boilerplate.api.token.dto.response.TokenResponseDto;
import com.example.boilerplate.api.token.entity.RefreshTokenEntity;
import com.example.boilerplate.api.token.repository.TokenRepository;
import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.api.user.repository.UserRepository;
import com.example.boilerplate.global.auth.TokenManager;
import com.example.boilerplate.global.auth.dto.internal.AccessTokenInternalDto;
import com.example.boilerplate.global.auth.dto.internal.RefreshTokenPayloadInternalDto;
import com.example.boilerplate.global.auth.dto.internal.TokenInternalDto;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenManager tokenManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public SuccessResponseDto<TokenResponseDto> createToken(CreateTokenRequestDto createTokenRequestDto) {
        userRepository.findByIdIfNullThrow(createTokenRequestDto.getUserId());

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                createTokenRequestDto.getUserId(),
                createTokenRequestDto.getUserRole()
        );

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(tokenInternalDto.getRefreshTokenInternalDto().getToken());

        UserEntity userEntity = userRepository.findByIdIfNullThrow(createTokenRequestDto.getUserId());
        userEntity.appendRefreshTokenEntity(refreshTokenEntity);
        userRepository.save(userEntity);

        return new SuccessResponseDto<>(TokenResponseDto.from(tokenInternalDto));
    }

    public SuccessResponseDto<AccessTokenResponseDto> renewToken(RenewTokenRequestDto renewTokenRequestDto) {
        RefreshTokenPayloadInternalDto payload = tokenManager.decodeRefreshToken(renewTokenRequestDto.getRefreshToken());

        RefreshTokenEntity refreshTokenEntity = tokenRepository.findByUserEntity_IdAndRefreshToken(
                payload.getUserId(),
                renewTokenRequestDto.getRefreshToken()
        );

        if (Objects.isNull(refreshTokenEntity))
            throw new BusinessException(CommonException.TOKEN_INVALID_EXCEPTION);

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                refreshTokenEntity.getUserEntity().getId(),
                refreshTokenEntity.getUserEntity().getUserRole()
        );

        AccessTokenInternalDto accessTokenInternalDto = tokenInternalDto.getAccessTokenInternalDto();

        return new SuccessResponseDto<>(AccessTokenResponseDto.from(accessTokenInternalDto));
    }
}
