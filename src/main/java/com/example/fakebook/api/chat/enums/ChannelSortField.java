package com.example.fakebook.api.chat.enums;

import com.example.fakebook.global.interfaces.SortField;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChannelSortField implements SortField {
    ID("id"), CREATED_AT("createdAt"), DE("description");
    private final String name;
}
