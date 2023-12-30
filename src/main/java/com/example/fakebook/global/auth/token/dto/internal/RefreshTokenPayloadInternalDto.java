package com.example.fakebook.global.auth.token.dto.internal;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuperBuilder
public class RefreshTokenPayloadInternalDto extends TokenPayloadInternalDto {
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", this.getUserId().toString());
        map.put("tokenType", this.getTokenType().name());
        return map;
    }
}
