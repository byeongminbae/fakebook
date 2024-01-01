package com.example.fakebook.api.member.repository;

import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long> {
    Member findBySignIdAndSignPasswordAndIsDeleted(String signId, String signPassword, boolean isDeleted);

    Member findBySignId(String signId);

    default Member findBySignIdAndSignPasswordAndIsDeletedThrowIfNull(String signId, String signPassword, boolean isDeleted) {
        Member member = findBySignIdAndSignPasswordAndIsDeleted(signId, signPassword, isDeleted);
        if (Objects.isNull(member))
            throw new BusinessException(CommonException.DB_NOT_FOUND_EXCEPTION);
        return member;
    }
}
