package com.example.boilerplate.global.auth.token.dto.internal;

import com.example.boilerplate.global.entity.UserRole;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuperBuilder
public class AccessTokenPayloadInternalDto extends TokenPayloadInternalDto {
    private final UserRole userRole;

    public Map<String, String> toMap(){
        Map<String, String> map = new HashMap<>();
        map.put("userId", this.getUserId().toString());
        map.put("userRole", this.getUserRole().name());
        map.put("tokenType", this.getTokenType().name());
        return map;
    }
}
