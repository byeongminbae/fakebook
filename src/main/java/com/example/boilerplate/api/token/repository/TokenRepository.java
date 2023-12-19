package com.example.boilerplate.api.token.repository;

import com.example.boilerplate.api.token.entity.RefreshTokenEntity;
import com.example.boilerplate.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends BaseRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity findByUserEntity_IdAndRefreshToken(Long id, String refreshToken);
}
