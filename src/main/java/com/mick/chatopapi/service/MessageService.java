package com.mick.chatopapi.service;

import com.mick.chatopapi.dto.MessageRequestDto;

public interface MessageService {

    void createMessage(MessageRequestDto messageRequestDto);
}
