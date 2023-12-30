package com.example.fakebook.global.repository;

import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    default T findByIdThrowIfNull(ID id) {
        Optional<T> entity = this.findById(id);
        entity.orElseThrow(() -> new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION));
        return entity.get();
    }
}
