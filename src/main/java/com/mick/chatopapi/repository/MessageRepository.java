package com.mick.chatopapi.repository;

import com.mick.chatopapi.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
