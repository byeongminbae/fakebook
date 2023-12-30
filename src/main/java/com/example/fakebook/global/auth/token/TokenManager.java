package com.example.fakebook.global.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fakebook.global.auth.token.dto.internal.*;
import com.example.fakebook.global.entity.UserRole;
import com.example.fakebook.global.enums.TokenType;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenManager {
    @Value("${token.secret}")
    private String secret;
    @Value("${token.access-token-expired-offset}")
    private long accessTokenExpiredOffset;
    @Value("${token.refresh-token-expired-offset}")
    private long refreshTokenExpiredOffset;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private LocalDateTime getExpiredAt(long offset) {
        return LocalDateTime.now().plus(offset, ChronoUnit.MILLIS);
    }

    private String createToken(Map<String, ?> claims, LocalDateTime expiredAt) {
        try {
            return JWT.create()
                    .withPayload(claims)
                    .withExpiresAt(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(getAlgorithm());
        } catch (
                JWTCreationException exception) {
            throw new BusinessException(CommonException.TOKEN_CREATION_EXCEPTION);
        }
    }

    private AccessTokenInternalDto createAccessToken(Long userId, UserRole userRole) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = AccessTokenPayloadInternalDto.builder()
                .userId(userId)
                .userRole(userRole)
                .tokenType(TokenType.ACCESS)
                .build();

        LocalDateTime expiredAt = getExpiredAt(accessTokenExpiredOffset);

        return AccessTokenInternalDto.builder()
                .token(createToken(accessTokenPayloadInternalDto.toMap(), expiredAt))
                .expiredAt(expiredAt)
                .build();
    }

    private RefreshTokenInternalDto createRefreshToken(Long userId) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = RefreshTokenPayloadInternalDto.builder()
                .userId(userId)
                .tokenType(TokenType.REFRESH)
                .build();

        LocalDateTime expiredAt = getExpiredAt(refreshTokenExpiredOffset);

        return RefreshTokenInternalDto.builder()
                .token(createToken(refreshTokenPayloadInternalDto.toMap(), expiredAt))
                .expiredAt(expiredAt)
                .build();
    }

    public TokenInternalDto createTokens(Long userId, UserRole userRole) {
        return TokenInternalDto.builder()
                .accessTokenInternalDto(createAccessToken(userId, userRole))
                .refreshTokenInternalDto(createRefreshToken(userId))
                .build();
    }

    public <T extends TokenPayloadInternalDto> T decodeToken(String token) {
        try {
            token = StringUtil.removeBearerPrefix(token);

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            TokenType tokenType = TokenType.valueOf(decodedJWT.getClaims().get("tokenType").asString());
            return tokenType.getTokenPayloadInternalDto(decodedJWT);
        } catch (TokenExpiredException exception) {
            throw new BusinessException(CommonException.TOKEN_EXPIRED_EXCEPTION);
        } catch (JWTVerificationException exception) {
            throw new BusinessException(CommonException.TOKEN_INVALID_EXCEPTION);
        }
    }
}
