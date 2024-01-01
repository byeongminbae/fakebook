package com.example.fakebook.api.member.repository;

import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long> {
    Member findBySignIdAndSignPasswordAndDeletedAtIsNull(String signId, String signPassword);

    Member findBySignId(String signId);
}
