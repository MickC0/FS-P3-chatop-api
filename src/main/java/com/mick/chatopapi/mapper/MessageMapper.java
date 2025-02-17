package com.mick.chatopapi.mapper;

import com.mick.chatopapi.dto.MessageRequestDto;
import com.mick.chatopapi.entity.MessageEntity;
import com.mick.chatopapi.entity.RentalEntity;
import com.mick.chatopapi.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageEntity toEntity(RentalEntity rental, UserEntity user,
                                  MessageRequestDto messageRequestDto) {
        return new MessageEntity(rental, user, messageRequestDto.message());
    }
}
