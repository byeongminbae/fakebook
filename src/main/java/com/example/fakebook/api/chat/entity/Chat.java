package com.example.fakebook.api.chat.entity;

import com.example.fakebook.api.member.entity.Member;
import com.example.fakebook.global.entity.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Chat extends Base {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Channel channel;

    private String content;
}
