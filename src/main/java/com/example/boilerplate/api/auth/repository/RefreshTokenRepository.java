package com.example.boilerplate.api.auth.repository;

import com.example.boilerplate.api.auth.entity.RefreshTokenEntity;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import com.example.boilerplate.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshTokenEntity, Long> {
    List<RefreshTokenEntity> findByRefreshTokenAndUserEntityId(String refreshToken, Long userId);

    default List<RefreshTokenEntity> findByRefreshTokenAndUserEntityIdThrowIfNull(String refreshToken, Long userId){
        List<RefreshTokenEntity> refreshTokenEntities = findByRefreshTokenAndUserEntityId(refreshToken, userId);
        if(refreshTokenEntities.isEmpty())
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return refreshTokenEntities;
    }
}
