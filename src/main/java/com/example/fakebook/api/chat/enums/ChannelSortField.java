package com.example.fakebook.api.chat.enums;

import com.example.fakebook.api.chat.entity.Channel;
import com.example.fakebook.api.common.entity.ChannelMember;
import com.example.fakebook.global.interfaces.SortField;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public enum ChannelSortField implements SortField {
    CREATED_AT("createdAt", "createdAt") {
        @Override
        public Expression<LocalDateTime> convertSortFieldValue(String sortFieldValue) {
            return Expressions.constant(LocalDateTime.parse(sortFieldValue));
        }
    },
    TITLE("title", "title"),
    DESCRIPTION( "description", "description"),
    ID("id", "memberCount");

    private final String entityFieldName;
    private final String dtoFieldName;
}
