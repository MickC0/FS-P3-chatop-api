package com.mick.chatopapi.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Le champ login est obligatoire")
        String email,
        @NotBlank(message = "Le champ password est obligatoire")
        String password) {
}
