package com.mick.chatopapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageRequestDto(
        @JsonProperty("rental_id")
        Integer rentalId,
        @JsonProperty("user_id")
        Integer userId,
        String message


){
}
