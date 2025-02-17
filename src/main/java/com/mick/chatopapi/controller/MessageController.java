package com.mick.chatopapi.controller;

import com.mick.chatopapi.dto.MessageRequestDto;
import com.mick.chatopapi.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> createMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            BindingResult bindingResult,
            Authentication authentication) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity
                        .badRequest()
                        .body(bindingResult.getAllErrors());
            }
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Utilisateur non authentifié");
            }

            messageService.createMessage(messageRequestDto);

            return ResponseEntity.ok(Map.of("message", "Message send with success"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Exception");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

