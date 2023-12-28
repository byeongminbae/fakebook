package com.example.boilerplate.api.auth.service;

import com.example.boilerplate.api.auth.dto.request.TokenRenewRequestDto;
import com.example.boilerplate.api.auth.dto.request.TokenSignInRequestDto;
import com.example.boilerplate.api.auth.dto.request.TokenSignOutRequestDto;
import com.example.boilerplate.api.auth.dto.response.AccessTokenResponseDto;
import com.example.boilerplate.api.auth.dto.response.RefreshTokenResponseDto;
import com.example.boilerplate.api.auth.dto.response.TokenResponseDto;
import com.example.boilerplate.api.auth.entity.RefreshTokenEntity;
import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.api.user.repository.UserRepository;
import com.example.boilerplate.global.auth.token.TokenManager;
import com.example.boilerplate.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.boilerplate.global.auth.token.dto.internal.RefreshTokenPayloadInternalDto;
import com.example.boilerplate.global.auth.token.dto.internal.TokenInternalDto;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import com.example.boilerplate.global.dto.response.SuccessVoidResponseDto;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import com.example.boilerplate.global.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenManager tokenManager;

    public SuccessResponseDto<TokenResponseDto> tokenSignIn(TokenSignInRequestDto tokenSignInRequestDto) {
        String encryptedSignPassword = CryptoUtil.encryptSha512(tokenSignInRequestDto.getSignPassword());

        UserEntity userEntity = userRepository.findBySignIdAndSignPassword(
                tokenSignInRequestDto.getSignId(),
                encryptedSignPassword
        );

        if (Objects.isNull(userEntity))
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);

        if (userEntity.getIsDeleted())
            throw new BusinessException(CommonException.DB_ALREADY_DELETED_EXCEPTION);

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                userEntity.getId(),
                userEntity.getUserRole()
        );

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(tokenInternalDto.getRefreshTokenInternalDto().getToken());
        refreshTokenEntity.setExpiredAt(tokenInternalDto.getRefreshTokenInternalDto().getExpiredAt());

        userEntity.addRefreshTokenEntity(refreshTokenEntity);
        userEntity.updateLastSignInAt();

        userRepository.save(userEntity);

        return new SuccessResponseDto<>(TokenResponseDto.from(tokenInternalDto));
    }

    public SuccessVoidResponseDto tokenSignOut(TokenSignOutRequestDto tokenSignOutRequestDto) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = tokenManager.decodeToken(
                tokenSignOutRequestDto.getRefreshToken()
        );

        UserEntity userEntity = userRepository.findByIdIfNullThrow(refreshTokenPayloadInternalDto.getUserId());
        userEntity.removeRefreshTokenEntity(tokenSignOutRequestDto.getRefreshToken());

        userRepository.save(userEntity);

        return new SuccessVoidResponseDto();
    }

    public SuccessResponseDto<AccessTokenResponseDto> tokenRenew(TokenRenewRequestDto tokenRenewRequestDto) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = tokenManager.decodeToken(
                tokenRenewRequestDto.getRefreshToken()
        );

        UserEntity userEntity = userRepository.findByIdIfNullThrow(refreshTokenPayloadInternalDto.getUserId());

        if (Objects.isNull(userEntity.findRefreshTokenEntity(tokenRenewRequestDto.getRefreshToken())))
            throw new BusinessException(CommonException.GLOBAL_NOT_FOUND);

        TokenInternalDto tokenInternalDto = tokenManager.createTokens(
                userEntity.getId(),
                userEntity.getUserRole()
        );

        return new SuccessResponseDto<>(AccessTokenResponseDto.from(tokenInternalDto.getAccessTokenInternalDto()));
    }

    public SuccessResponseDto<List<RefreshTokenResponseDto>> getRefreshTokenList(String authorizationHeader){
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(authorizationHeader);

        UserEntity userEntity = userRepository.findByIdIfNullThrow(accessTokenPayloadInternalDto.getUserId());

        List<RefreshTokenResponseDto> refreshTokenResponseDtos = userEntity.getRefreshTokenEntities().stream()
                .map(RefreshTokenResponseDto::from)
                .toList();

        return new SuccessResponseDto<>(refreshTokenResponseDtos);
    }
}
