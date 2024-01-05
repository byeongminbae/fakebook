package com.example.fakebook.api.auth.repository;

import com.example.fakebook.api.auth.entity.RefreshToken;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {
    List<RefreshToken> findByToken(String token);
    default List<RefreshToken> findByTokenThrowIfNull(String refreshToken) {
        List<RefreshToken> refreshTokenEntities = findByToken(refreshToken);
        if (refreshTokenEntities.isEmpty())
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return refreshTokenEntities;
    }
}
