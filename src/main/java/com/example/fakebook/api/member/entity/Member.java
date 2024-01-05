package com.example.fakebook.api.member.entity;

import com.example.fakebook.api.auth.entity.RefreshToken;
import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.chat.entity.Chat;
import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.global.entity.Base;
import com.example.fakebook.global.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(indexes = {
        @Index(name = "index_sign_id", columnList = "signId", unique = true),
        @Index(name = "index_sign_id_and_sign_password", columnList = "signId, signPassword"),
        @Index(name = "index_email", columnList = "email"),
        @Index(name = "index_phone_number", columnList = "phoneNumber"),
})
public class Member extends Base {
    @Setter
    private String signId;
    @Setter
    private String signPassword;
    private LocalDateTime lastSignInAt = LocalDateTime.now();

    @Setter
    private String email;
    @Setter
    private String phoneNumber;

    @Setter
    private LocalDateTime blacklistedAt;

    @Setter
    private Role role = Role.USER;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<ChannelMember> channelMembers = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<Channel> channels = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<Chat> chats = new ArrayList<>();

    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setMember(this);
    }

    public void removeRefreshToken(String token) {
        refreshTokens.removeIf((entity) -> token.equals(entity.getToken()));
    }

    public void applyBlacklist() {
        blacklistedAt = LocalDateTime.now();
    }

    public void revokeBlacklist() {
        blacklistedAt = null;
    }

    public boolean containChannel(Channel channel) {
        for (ChannelMember channelMember : getChannelMembers()) {
            if (channelMember.getChannel().equals(channel)) {
                return true;
            }
        }
        return false;
    }

    public void updateLastSignInAt() {
        lastSignInAt = LocalDateTime.now();
    }
}
