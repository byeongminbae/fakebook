package com.example.fakebook.api.chat.entity;

import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.entity.Base;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Channel extends Base {
    @Setter
    private String title;
    @Setter
    private String description;

    @Setter
    @ManyToOne
    private Member creator;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.PERSIST)
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.PERSIST)
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.setChannel(this);
    }

    public void addChannelMember(ChannelMember channelMember) {
        channelMembers.add(channelMember);
        channelMember.setChannel(this);
    }
}
