package com.example.boilerplate.api.user.repository;

import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    UserEntity findBySignIdAndSignPassword(String signId, String signPassword);
    UserEntity findBySignId(String signId);
}
