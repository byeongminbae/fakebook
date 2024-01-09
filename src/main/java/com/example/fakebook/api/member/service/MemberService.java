package com.example.fakebook.api.member.service;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.repository.ChannelRepository;
import com.example.fakebook.api.common.dto.response.GetChannelResponseDto;
import com.example.fakebook.api.common.dto.response.GetChatResponseDto;
import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.api.member.dto.request.CreateMemberRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberPasswordRequestDto;
import com.example.fakebook.api.member.dto.request.UpdateMemberRequestDto;
import com.example.fakebook.api.member.dto.response.*;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.api.member.util.MemberUtil;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.dto.response.SuccessDataResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import com.example.fakebook.global.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;
    private final ChannelRepository channelRepository;


    public SuccessDataResponseDto<CreateMemberResponseDto> createMember(CreateMemberRequestDto createMemberRequestDto) {
        Member duplicatedMember = memberRepository.findBySignId(createMemberRequestDto.getSignId());

        if (Objects.nonNull(duplicatedMember))
            throw new BusinessException(CommonException.DB_ALREADY_EXIST_EXCEPTION);


        String encryptedSignPassword = CryptoUtil.encryptSha512(createMemberRequestDto.getSignPassword());

        Member member = new Member();
        member.setSignId(createMemberRequestDto.getSignId());
        member.setSignPassword(encryptedSignPassword);
        member.setEmail(createMemberRequestDto.getEmail());
        member.setPhoneNumber(createMemberRequestDto.getPhoneNumber());

        Member savedMember = memberRepository.save(member);

        return new SuccessDataResponseDto<>(CreateMemberResponseDto.from(savedMember));
    }

    public SuccessDataResponseDto<GetMemberInfoResponseDto> getMember(Long memberId, String authorizationHeader) {
        Member opponent = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);

        if (Objects.nonNull(authorizationHeader)) {
            Member requester = memberUtil.getOwnerByAccessToken(authorizationHeader);

            if (opponent.equals(requester))
                return new SuccessDataResponseDto<>(GetMemberPrivateInfoResponseDto.from(opponent));
        }

        return new SuccessDataResponseDto<>(GetMemberPublicInfoResponseDto.from(opponent));
    }

    public SuccessVoidResponseDto updateMember(
            Long memberId,
            UpdateMemberRequestDto updateMemberRequestDto
    ) {
        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);

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
        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);

        String oldSignPassword = updateMemberPasswordRequestDto.getOldSignPassword();
        String newSignPassword = updateMemberPasswordRequestDto.getNewSignPassword();
        String confirmSignPassword = updateMemberPasswordRequestDto.getConfirmSignPassword();

        if (!(oldSignPassword.equals(member.getSignPassword()) && newSignPassword.equals(confirmSignPassword)))
            throw new BusinessException(CommonException.GLOBAL_INVALID_INPUT_EXCEPTION);

        member.setSignPassword(updateMemberPasswordRequestDto.getNewSignPassword());
        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto deleteMember(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);
        member.delete();
        memberRepository.save(member);

        return new SuccessVoidResponseDto();
    }

    public SuccessDataResponseDto<List<GetChannelResponseDto>> getChannels(Long memberId){
        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);
        List<ChannelMember> channelMembers = member.getChannelMembers();
        List<GetChannelResponseDto> getChannelResponseDtos = channelMembers.stream()
                .map((channelMember)-> GetChannelResponseDto.from(channelMember.getChannel()))
                .collect(Collectors.toList());

        return new SuccessDataResponseDto<>(getChannelResponseDtos);
    }

    public SuccessDataResponseDto<List<GetChatResponseDto>> getChats(Long memberId, Long channelId){
        Channel channel = channelRepository.findByIdAndChannelMembersMemberIdAndDeletedAtIsNullThrowIfNull(
                channelId,
                memberId
        );

//        channel.getChannelMembers().stream()
//                .filter((channelMember) -> channelMember.getMember().getId().equals(memberId))
//                .findFirst()
//                .orElseThrow(()-> new BusinessException(CommonException.CHAT_UNAUTHORIZED_EXCEPTION));

        List<GetChatResponseDto> chatResponseDtos = channel.getChats().stream()
                .map(GetChatResponseDto::from)
                .collect(Collectors.toList());

        return new SuccessDataResponseDto<>(chatResponseDtos);
    }
}
