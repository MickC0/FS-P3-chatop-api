package com.mick.chatopapi.dto;

public record ErrorResponse(String error, int status, String reason) {
}
