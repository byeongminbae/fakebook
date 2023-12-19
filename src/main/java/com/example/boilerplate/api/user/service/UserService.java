package com.example.boilerplate.api.user.service;

import com.example.boilerplate.api.user.dto.request.CreateUserRequestDto;
import com.example.boilerplate.api.user.dto.request.ReplaceUserRequestDto;
import com.example.boilerplate.api.user.dto.request.UpdateUserRequestDto;
import com.example.boilerplate.api.user.dto.response.CreateUserResponseDto;
import com.example.boilerplate.api.user.dto.response.GetUserResponseDto;
import com.example.boilerplate.api.user.dto.response.ReplaceUserResponseDto;
import com.example.boilerplate.api.user.dto.response.UpdateUserResponseDto;
import com.example.boilerplate.api.user.entity.UserEntity;
import com.example.boilerplate.global.entity.UserStatus;
import com.example.boilerplate.api.user.repository.UserRepository;
import com.example.boilerplate.global.dto.response.SuccessResponseDto;
import com.example.boilerplate.global.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public SuccessResponseDto<GetUserResponseDto> getUser(Long userId) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);
        GetUserResponseDto getUserResponseDto = GetUserResponseDto.from(userEntity);
        return new SuccessResponseDto<>(getUserResponseDto);
    }

    public SuccessResponseDto<UpdateUserResponseDto> updateUser(
            Long userId,
            UpdateUserRequestDto updateUserRequestDto
    ) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);

        String email = updateUserRequestDto.getEmail();
        String nickname = updateUserRequestDto.getNickname();

        // TODO: refactoring
        userEntity.setNickname(Objects.isNull(nickname) ? userEntity.getNickname() : nickname);
        userEntity.setEmail(Objects.isNull(email) ? userEntity.getEmail() : email);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return new SuccessResponseDto<>(UpdateUserResponseDto.from(savedUserEntity));
    }

    public SuccessResponseDto<ReplaceUserResponseDto> replaceUser(
            Long userId,
            ReplaceUserRequestDto replaceUserRequestDto
    ) {
        UserEntity userEntity = userRepository.findByIdIfNullThrow(userId);

        userEntity.setNickname(replaceUserRequestDto.getNickname());
        userEntity.setEmail(replaceUserRequestDto.getEmail());

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return new SuccessResponseDto<>(ReplaceUserResponseDto.from(savedUserEntity));
    }

    public SuccessResponseDto<CreateUserResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {
        UserEntity userEntity = new UserEntity();

        userEntity.setNickname(createUserRequestDto.getNickname());
        userEntity.setEmail(createUserRequestDto.getEmail());
        userEntity.setUserRole(UserRole.USER);
        userEntity.setUserStatus(UserStatus.NORMAL);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return new SuccessResponseDto<>(CreateUserResponseDto.from(savedUserEntity));
    }

    public SuccessResponseDto<Object> deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return new SuccessResponseDto<>();
    }
}
