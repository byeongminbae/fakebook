package com.example.fakebook.global.auth.aop;

import com.example.fakebook.global.auth.token.TokenManager;
import com.example.fakebook.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.fakebook.global.entity.UserRole;
import com.example.fakebook.global.exception.BusinessException;
import com.example.fakebook.global.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {
    private final TokenManager tokenManager;

    private HttpServletRequest getHttpServletRequest() {
        if (Objects.isNull(RequestContextHolder.getRequestAttributes()))
            throw new BusinessException(CommonException.TOKEN_UNKNOWN_EXCEPTION);

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private String getBearerToken() {
        String authorizationHeader = getHttpServletRequest().getHeader("Authorization");

        if (Objects.isNull(authorizationHeader))
            throw new BusinessException(CommonException.TOKEN_NOT_FOUND_EXCEPTION);

        return authorizationHeader;
    }

    private Auth getAuthAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(Auth.class);
    }

    private void validateUserRole(Set<UserRole> userRoles, UserRole userRole) {
        if (!userRoles.contains(userRole))
            throw new BusinessException(CommonException.TOKEN_UNAUTHORIZED_EXCEPTION);
    }

    @SuppressWarnings(value = "unchecked")
    private void validateUserId(String userIdFieldName, Long tokenUserId) {
        if (!userIdFieldName.isEmpty()) {
            Map<String, String> pathVariables = (Map<String, String>) getHttpServletRequest()
                    .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            Long parsedUserId = Long.parseLong(pathVariables.get(userIdFieldName));

            if (!parsedUserId.equals(tokenUserId))
                throw new BusinessException(CommonException.TOKEN_UNAUTHORIZED_EXCEPTION);
        }
    }

    @Before("@annotation(com.example.fakebook.global.auth.aop.Auth)")
    public void before(JoinPoint joinPoint) {
        String token = getBearerToken();

        AccessTokenPayloadInternalDto payload = tokenManager.decodeToken(token);

        Auth authAnnotation = getAuthAnnotation(joinPoint);

        Set<UserRole> userRole = Set.of(authAnnotation.userRoles());
        String userIdFieldName = authAnnotation.pathVariableUserId();

        validateUserRole(userRole, payload.getUserRole());
        validateUserId(userIdFieldName, payload.getUserId());
    }
}
