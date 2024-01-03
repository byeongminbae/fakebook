package com.example.fakebook.api.chat.service;

import com.example.fakebook.api.chat.dto.request.CreateChannelRequestDto;
import com.example.fakebook.api.chat.dto.response.CreateChannelResponseDto;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.repository.ChannelRepository;
import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.api.member.repository.MemberRepository;
import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.dto.response.SuccessResponseDto;
import com.example.fakebook.global.dto.response.SuccessVoidResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;

    public SuccessVoidResponseDto getChannels() {
        return null;
    }

    public SuccessResponseDto<CreateChannelResponseDto> createChannel(
            String authorizationHeader,
            CreateChannelRequestDto createChannelRequestDto
    ) {
        AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(authorizationHeader);

        Member creator = memberRepository.findByIdAndDeletedAtIsNullThrowIfNull(
                accessTokenPayloadInternalDto.getMemberId()
        );

        ChannelMember channelMember = new ChannelMember();
        Channel channel = new Channel();

        channel.setCreator(creator);
        channel.addChannelMember(channelMember);
        channel.setTitle(createChannelRequestDto.getTitle());
        channel.setDescription(createChannelRequestDto.getDescription());

        channelMember.setMember(creator);
        channelMember.setChannel(channel);

        Channel savedChannel = channelRepository.save(channel);

        return new SuccessResponseDto<>(CreateChannelResponseDto.from(savedChannel));
    }

    public SuccessVoidResponseDto deleteChannel() {
        return null;
    }

    public SuccessVoidResponseDto joinChannel() {
        return null;
    }

    public SuccessVoidResponseDto exitChannel() {
        return null;
    }
}
