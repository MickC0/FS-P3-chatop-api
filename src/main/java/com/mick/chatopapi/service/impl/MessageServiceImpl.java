package com.mick.chatopapi.service.impl;

import com.mick.chatopapi.dto.MessageRequestDto;
import com.mick.chatopapi.entity.MessageEntity;
import com.mick.chatopapi.entity.RentalEntity;
import com.mick.chatopapi.entity.UserEntity;
import com.mick.chatopapi.mapper.MessageMapper;
import com.mick.chatopapi.repository.MessageRepository;
import com.mick.chatopapi.repository.RentalRepository;
import com.mick.chatopapi.repository.UserRepository;
import com.mick.chatopapi.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper, UserRepository userRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }


    @Override
    public void createMessage(MessageRequestDto messageRequestDto) {
        UserEntity user = userRepository.findById(messageRequestDto.userId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        RentalEntity rental = rentalRepository.findById(messageRequestDto.rentalId()).orElseThrow(
                () -> new RuntimeException("Rental not found")
        );

        MessageEntity messageEntity = messageMapper.toEntity(rental, user, messageRequestDto);
        messageEntity.setCreated_at(LocalDateTime.now());
        messageEntity.setUpdated_at(LocalDateTime.now());
        messageRepository.save(messageEntity);
    }
}
