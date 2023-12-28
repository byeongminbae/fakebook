package com.example.boilerplate.api.user.service;

import com.example.boilerplate.api.user.dto.request.CreateUserRequestDto;
import com.example.boilerplate.api.user.dto.request.UpdateUserPasswordRequestDto;
import com.example.boilerplate.api.user.dto.request.UpdateUserRequestDto;
import com.example.boilerplate.api.user.dto.response.CreateUserResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserInfoResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserPrivateInfoResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserPublicInfoResponseDto;
import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.api.user.repository.UserRepository;
import com.example.boilerplate.global.auth.token.TokenManager;
import com.example.boilerplate.global.auth.token.dto.internal.AccessTokenPayloadInternalDto;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import com.example.boilerplate.global.dto.response.SuccessVoidResponseDto;
import com.example.boilerplate.global.exception.BusinessException;
import com.example.boilerplate.global.exception.CommonException;
import com.example.boilerplate.global.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    public SuccessResponseDto<CreateUserResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {
        UserEntity duplicatedUserEntity = userRepository.findBySignId(createUserRequestDto.getSignId());

        if (!Objects.isNull(duplicatedUserEntity))
            throw new BusinessException(CommonException.DB_ALREADY_EXIST_EXCEPTION);


        String encryptedSignPassword = CryptoUtil.encryptSha512(createUserRequestDto.getSignPassword());

        UserEntity userEntity = new UserEntity();
        userEntity.setSignId(createUserRequestDto.getSignId());
        userEntity.setSignPassword(encryptedSignPassword);
        userEntity.setEmail(createUserRequestDto.getEmail());
        userEntity.setPhoneNumber(createUserRequestDto.getPhoneNumber());

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return new SuccessResponseDto<>(CreateUserResponseDto.from(savedUserEntity));
    }

    public SuccessResponseDto<GetUserInfoResponseDto> getUser(Long userId, String authorizationHeader) {
        UserEntity opponentUserEntity = userRepository.findByIdIfNullThrow(userId);

        if (!Objects.isNull(authorizationHeader)) {
            AccessTokenPayloadInternalDto accessTokenPayloadInternalDto = tokenManager.decodeToken(authorizationHeader);

            UserEntity requesterUserEntity = userRepository.findByIdIfNullThrow(
                    accessTokenPayloadInternalDto.getUserId()
            );

            if (opponentUserEntity.equals(requesterUserEntity))
                return new SuccessResponseDto<>(GetUserPrivateInfoResponseDto.from(opponentUserEntity));
        }

        return new SuccessResponseDto<>(GetUserPublicInfoResponseDto.from(opponentUserEntity));
    }

    public SuccessVoidResponseDto updateUser(
            Long userId,
            UpdateUserRequestDto updateUserRequestDto
    ) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);

        userEntity.setEmail(Objects.isNull(updateUserRequestDto.getEmail()) ?
                userEntity.getEmail() : updateUserRequestDto.getEmail());

        userEntity.setEmail(Objects.isNull(updateUserRequestDto.getEmail()) ?
                userEntity.getEmail() : updateUserRequestDto.getEmail());

        userRepository.save(userEntity);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto updateUserPassword(
            Long userId,
            UpdateUserPasswordRequestDto updateUserPasswordRequestDto
    ) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);

        String oldSignPassword = updateUserPasswordRequestDto.getOldSignPassword();
        String newSignPassword = updateUserPasswordRequestDto.getNewSignPassword();
        String confirmSignPassword = updateUserPasswordRequestDto.getConfirmSignPassword();

        if (!(oldSignPassword.equals(userEntity.getSignPassword()) && newSignPassword.equals(confirmSignPassword)))
            throw new BusinessException(CommonException.GLOBAL_INVALID_INPUT);

        userEntity.setSignPassword(updateUserPasswordRequestDto.getNewSignPassword());
        userRepository.save(userEntity);

        return new SuccessVoidResponseDto();
    }

    public SuccessVoidResponseDto deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);
        userEntity.deleteUser();
        userRepository.save(userEntity);

        return new SuccessVoidResponseDto();
    }
}
