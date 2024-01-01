package com.example.fakebook.api.member.service;

import com.example.fakebook.api.member.dto.request.CreateMemberRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberPasswordRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberRequestDto;
import com.example.fakebook.api.member.dto.response.CreateMemberResponseDto;
import com.example.fakebook.api.member.dto.response.GetMemberInfoResponseDto;
import com.example.fakebook.api.member.dto.response.GetMemberPrivateInfoResponseDto;
import com.example.fakebook.api.member.dto.response.GetMemberPublicInfoResponseDto;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.api.member.util.MemberUtil;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.dto.response.SuccessResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;

    public SuccessResponseDto<CreateMemberResponseDto> createMember(CreateMemberRequestDto createMemberRequestDto) {
        Member duplicatedMember = memberRepository.findBySignId(createMemberRequestDto.getSignId());

        if (!Objects.isNull(duplicatedMember))
            throw new BusinessException(CommonException.DB_ALREADY_EXIST_EXCEPTION);


        String encryptedSignPassword = CryptoUtil.encryptSha512(createMemberRequestDto.getSignPassword());

        Member member = new Member();
        member.setSignId(createMemberRequestDto.getSignId());
        member.setSignPassword(encryptedSignPassword);
        member.setEmail(createMemberRequestDto.getEmail());
        member.setPhoneNumber(createMemberRequestDto.getPhoneNumber());

        Member savedMember = memberRepository.save(member);

        return new SuccessResponseDto<>(CreateMemberResponseDto.from(savedMember));
    }

    public SuccessResponseDto<GetMemberInfoResponseDto> getMember(Long memberId, String authorizationHeader) {
        Member opponent = memberRepository.findByIdThrowIfNull(memberId);

        if (!Objects.isNull(authorizationHeader)) {
            Member requester = memberUtil.getOwnerByAccessToken(authorizationHeader);

            if (opponent.equals(requester))
                return new SuccessResponseDto<>(GetMemberPrivateInfoResponseDto.from(opponent));
        }

        return new SuccessResponseDto<>(GetMemberPublicInfoResponseDto.from(opponent));
    }

    public SuccessVoidResponseDto updateMember(
            Long memberId,
            UpdateMemberRequestDto updateMemberRequestDto
    ) {
        Member member = memberRepository.findByIdThrowIfNull(memberId);

        member.setEmail(Objects.isNull(updateMemberRequestDto.getEmail()) ?
                member.getEmail() : updateMemberRequestDto.getEmail());

        member.setEmail(Objects.isNull(updateMemberRequestDto.getEmail()) ?
                member.getEmail() : updateMemberRequestDto.getEmail());

        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto updateMemberPassword(
            Long memberId,
            UpdateMemberPasswordRequestDto updateMemberPasswordRequestDto
    ) {
        Member member = memberRepository.findByIdThrowIfNull(memberId);

        String oldSignPassword = updateMemberPasswordRequestDto.getOldSignPassword();
        String newSignPassword = updateMemberPasswordRequestDto.getNewSignPassword();
        String confirmSignPassword = updateMemberPasswordRequestDto.getConfirmSignPassword();

        if (!(oldSignPassword.equals(member.getSignPassword()) && newSignPassword.equals(confirmSignPassword)))
            throw new BusinessException(CommonException.GLOBAL_INVALID_INPUT);

        member.setSignPassword(updateMemberPasswordRequestDto.getNewSignPassword());
        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto deleteMember(Long memberId) {
        Member member = memberRepository.findByIdThrowIfNull(memberId);
        member.delete();
        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }
}
