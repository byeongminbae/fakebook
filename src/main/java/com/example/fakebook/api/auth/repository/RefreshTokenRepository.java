package com.example.fakebook.api.auth.repository;

import com.example.fakebook.api.auth.entity.RefreshToken;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {
    List<RefreshToken> findByTokenAndMemberId(String token, Long memberId);

    default List<RefreshToken> findByTokenAndMemberIdThrowIfNull(String refreshToken, Long memberId) {
        List<RefreshToken> refreshTokenEntities = findByTokenAndMemberId(refreshToken, memberId);
        if (refreshTokenEntities.isEmpty())
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return refreshTokenEntities;
    }
}
