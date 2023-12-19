package com.example.boilerplate.global.auth.dto.internal;

import com.example.boilerplate.global.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class AccessTokenPayloadInternalDto {
    private final Long userId;
    private final UserRole userRole;

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId.toString());
        map.put("userRole", userRole.name());
        return map;
    }
}
