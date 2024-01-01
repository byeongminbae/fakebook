package com.example.fakebook.global.auth.token.dto.internal;

import com.example.fakebook.global.enums.Role;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Getter
@SuperBuilder
public class AccessTokenPayloadInternalDto extends TokenPayloadInternalDto {
    private final Role role;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("memberId", this.getMemberId().toString());
        map.put("role", this.getRole().name());
        map.put("tokenType", this.getTokenType().name());
        return map;
    }
}
