package com.example.fakebook.api.common.entity;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChannelMember extends Base {
    @ManyToOne
    private Channel channel;
    @ManyToOne
    private Member member;
}
