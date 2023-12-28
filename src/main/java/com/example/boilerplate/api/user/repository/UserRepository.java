package com.example.boilerplate.api.user.repository;

import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import com.example.boilerplate.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    UserEntity findBySignIdAndSignPasswordAndIsDeleted(String signId, String signPassword, boolean isDeleted);
    UserEntity findBySignId(String signId);

    default UserEntity findBySignIdAndSignPasswordAndIsDeletedThrowIfNull(String signId, String signPassword, boolean isDeleted){
        UserEntity userEntity = findBySignIdAndSignPasswordAndIsDeleted(signId, signPassword, isDeleted);
        if(Objects.isNull(userEntity))
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return userEntity;
    }
}
