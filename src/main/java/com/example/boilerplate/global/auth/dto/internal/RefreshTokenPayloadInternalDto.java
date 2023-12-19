package com.example.boilerplate.global.auth.dto.internal;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class RefreshTokenPayloadInternalDto {
    private final Long userId;

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        return map;
    }
}
