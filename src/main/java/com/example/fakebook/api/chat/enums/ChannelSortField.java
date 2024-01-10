package com.example.fakebook.api.chat.enums;

import com.example.fakebook.global.interfaces.SortField;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

//
//@Getter
//@RequiredArgsConstructor
//public enum ChannelSortField implements SortField {
//    CREATED_AT("createdAt", LocalDateTime.class, QChannel.channel.title),
//    TITLE("title", String.class),
//    DESCRIPTION("description", String.class);
//
//    private final String fieldName;
//    private final Class<?> fieldClass;
//
//}

@Getter
@RequiredArgsConstructor
public enum ChannelSortField implements SortField {
    CREATED_AT("createdAt", LocalDateTime.class) {
        @Override
        public Expression<LocalDateTime> convertSortFieldValue(String sortFieldValue) {
            return Expressions.constant(LocalDateTime.parse(sortFieldValue));
        }
    },
    TITLE("title", String.class) {
        @Override
        public Expression<String> convertSortFieldValue(String sortFieldValue) {
            return Expressions.constant(sortFieldValue);
        }
    },
    DESCRIPTION("description", String.class) {
        @Override
        public Expression<String> convertSortFieldValue(String sortFieldValue) {
            return Expressions.constant(sortFieldValue);
        }
    };

    private final String fieldName;
    private final Class<?> fieldClass;
}
