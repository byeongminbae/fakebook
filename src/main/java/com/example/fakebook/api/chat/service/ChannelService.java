package com.example.fakebook.api.chat.service;

import com.example.fakebook.api.chat.dto.request.CreateChannelRequestDto;
import com.example.fakebook.api.chat.dto.request.GetChannelRequestDto;
import com.example.fakebook.api.chat.dto.response.CreateChannelResponseDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.repository.ChannelCustomRepository;
import com.example.fakebook.api.chat.repository.ChannelRepository;
import com.example.fakebook.api.common.dto.response.GetChannelResponseDto;
import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.dto.response.CursorPaginationResponseDto;
import com.example.fakebook.global.dto.response.SuccessDataResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelCustomRepository channelCustomRepository;
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;

    public CursorPaginationResponseDto<GetChannelResponseDto> getChannels(GetChannelRequestDto getChannelRequestDto) {
        List<Channel> channels = channelCustomRepository.find(getChannelRequestDto);

        return CursorPaginationResponseDto.from(
                getChannelRequestDto,
                channels,
                GetChannelResponseDto::from
        );
    }

    public SuccessDataResponseDto<CreateChannelResponseDto> createChannel(
            String authorizationHeader,
            CreateChannelRequestDto createChannelRequestDto
    ) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(authorizationHeader);

        Member creator = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(
                accessTokenPayloadInternalDto.getMemberId()
        );

        Channel duplicatedChannel = channelRepository.findByTitle(createChannelRequestDto.getTitle());
        if (Objects.nonNull(duplicatedChannel)) {
            throw new BusinessException(CommonException.DB_ALREADY_EXIST_EXCEPTION);
        }

        Channel channel = new Channel();
        ChannelMember channelMember = new ChannelMember();

        channel.setCreator(creator);
        channel.addChannelMember(channelMember);
        channel.setTitle(createChannelRequestDto.getTitle());
        channel.setDescription(createChannelRequestDto.getDescription());

        channelMember.setMember(creator);
        channelMember.setChannel(channel);

        Channel savedChannel = channelRepository.save(channel);

        return new SuccessDataResponseDto<>(CreateChannelResponseDto.from(savedChannel));
    }

    public SuccessVoidResponseDto deleteChannel() {
        return null;
    }

    public SuccessVoidResponseDto joinChannel(Long channelId, Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(memberId);
        Channel channel = channelRepository.findByIdAndDeletedAtIsNullThrowIfNull(channelId);

        if (member.containChannel(channel))
            throw new BusinessException(CommonException.DB_ALREADY_EXIST_EXCEPTION);

        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannel(channel);
        channelMember.setMember(member);
        channel.addChannelMember(channelMember);

        channelRepository.save(channel);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto exitChannel() {
        return null;
    }
}
