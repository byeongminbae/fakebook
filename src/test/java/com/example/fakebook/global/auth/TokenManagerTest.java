//package com.example.fakebook.global.auth;
//
//import com.example.fakebook.api.user.entity.Member;
//import com.example.fakebook.api.user.repository.MemberRepository;
//import com.example.fakebook.global.auth.token.dto.internal.TokenInternalDto;
//import com.example.fakebook.global.auth.token.TokenManager;
//import com.example.fakebook.global.enums.Role;
//import com.example.fakebook.global.exception.BusinessException;
//import com.example.fakebook.global.exception.CommonException;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@SpringBootTest
//class TokenManagerTest {
//    @Autowired
//    private TokenManager tokenManager;
//    @Autowired
//    private MemberRepository userRepository;
//
//    private Member createUser() {
//        Member userEntity = new Member();
//
//        userEntity.setEmail("example@example.com");
//        userEntity.setNickname("example");
//        userEntity.setRole(Role.USER);
//        userEntity.setUserStatus(UserStatus.NORMAL);
//
//        return userRepository.save(userEntity);
//    }
//
//    @Test
//    void accessTokenExpired() {
//        // given
//        ReflectionTestUtils.setField(tokenManager, "accessTokenExpiredOffset", 0);
//        Member userEntity = createUser();
//        TokenInternalDto tokenInternalDto = tokenManager.createTokens(userEntity.getId(), userEntity.getRole());
//
//        // when, then
//        Assertions.assertThatThrownBy(() -> {
//            tokenManager.decodeAccessToken(tokenInternalDto.getAccessTokenInternalDto().getToken());
//        }).isInstanceOf(BusinessException.class).hasMessage(CommonException.TOKEN_EXPIRED_EXCEPTION.getMessage());
//    }
//}