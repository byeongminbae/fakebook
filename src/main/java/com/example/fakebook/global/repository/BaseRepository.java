package com.example.fakebook.global.repository;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Objects;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    T findByIdAndDeletedAtIsNull(ID id);

    default T findByIdThrowIfNull(ID id) {
        Optional<T> optionalEntity = findById(id);
        optionalEntity.orElseThrow(() -> new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION));
        return optionalEntity.get();
    }

    default T findByIdAndDeletedAtIsNullThrowIfNull(ID id) {
        T entity = findByIdAndDeletedAtIsNull(id);
        if (Objects.isNull(entity))
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return entity;
    }
}
