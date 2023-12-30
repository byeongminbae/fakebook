package com.example.fakebook.api.auth.repository;

import com.example.fakebook.api.auth.entity.RefreshTokenEntity;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshTokenEntity, Long> {
    List<RefreshTokenEntity> findByRefreshTokenAndUserEntityId(String refreshToken, Long userId);

    default List<RefreshTokenEntity> findByRefreshTokenAndUserEntityIdThrowIfNull(String refreshToken, Long userId) {
        List<RefreshTokenEntity> refreshTokenEntities = findByRefreshTokenAndUserEntityId(refreshToken, userId);
        if (refreshTokenEntities.isEmpty())
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return refreshTokenEntities;
    }
}
