package com.example.boilerplate.global.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.boilerplate.global.auth.dto.internal.*;
import com.example.boilerplate.global.entity.UserRole;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenManager {
    @Value("${token.secret}")
    private String secret;
    @Value("${token.access-token-expired-offset}")
    private int accessTokenExpiredOffset;
    @Value("${token.refresh-token-expired-offset}")
    private int refreshTokenExpiredOffset;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private Date getExpiredAt(int offset) {
        return new Date(System.currentTimeMillis() + offset);
    }

    private String createToken(Map<String, ?> claims, Date expiredAt) {

        try {
            return JWT.create()
                    .withPayload(claims)
                    .withExpiresAt(expiredAt)
                    .sign(getAlgorithm());
        } catch (
                JWTCreationException exception) {
            throw new BusinessException(CommonException.TOKEN_CREATION_EXCEPTION);
        }
    }

    private Map<String, Claim> decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT.getClaims();
        } catch (TokenExpiredException exception) {
            throw new BusinessException(CommonException.TOKEN_EXPIRED_EXCEPTION);
        } catch (JWTVerificationException exception) {
            throw new BusinessException(CommonException.TOKEN_INVALID_EXCEPTION);
        }
    }

    private AccessTokenInternalDto createAccessToken(Long userId, UserRole userRole) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = AccessTokenPayloadInternalDto.builder()
                .userId(userId)
                .userRole(userRole)
                .build();

        Date expiredAt = getExpiredAt(accessTokenExpiredOffset);

        return AccessTokenInternalDto.builder()
                .token(createToken(accessTokenPayloadInternalDto.toMap(), expiredAt))
                .expiredAt(expiredAt)
                .build();
    }

    private RefreshTokenInternalDto createRefreshToken(Long userId) {
        RefreshTokenPayloadInternalDto refreshTokenPayloadInternalDto = RefreshTokenPayloadInternalDto.builder()
                .userId(userId)
                .build();

        Date expiredAt = getExpiredAt(refreshTokenExpiredOffset);

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

    public AccessTokenPayloadInternalDto decodeAccessToken(String token) {
        Map<String, Claim> decodedToken = decodeToken(token);
        return AccessTokenPayloadInternalDto.builder()
                .userId(Long.parseLong(decodedToken.get("userId").asString()))
                .userRole(UserRole.valueOf(decodedToken.get("userRole").asString()))
                .build();
    }

    public RefreshTokenPayloadInternalDto decodeRefreshToken(String token) {
        Map<String, Claim> decodedToken = decodeToken(token);
        return RefreshTokenPayloadInternalDto.builder()
                .userId(Long.parseLong(decodedToken.get("userId").asString()))
                .build();
    }
}
