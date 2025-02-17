package com.mick.chatopapi.dto;

import java.time.LocalDateTime;

public record UserDto(
        Integer id,
        String email,
        String name,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
