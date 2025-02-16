package com.mick.chatopapi.mapper;

import com.mick.chatopapi.dto.RegisterRequest;
import com.mick.chatopapi.dto.UserDto;
import com.mick.chatopapi.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(RegisterRequest registerRequest, String hashedPassword) {
        return new UserEntity(registerRequest.email(), registerRequest.name(), hashedPassword);
    }

    public UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getCreated_at(),
                userEntity.getUpdated_at()
        );
    }
}
