package com.mick.chatopapi.service;

import com.mick.chatopapi.dto.AuthResponse;
import com.mick.chatopapi.dto.LoginRequest;
import com.mick.chatopapi.dto.RegisterRequest;
import com.mick.chatopapi.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    AuthResponse login(LoginRequest loginRequest) throws Exception;
    AuthResponse register(RegisterRequest registerRequest);
    UserDto getAuthenticatedUser(Authentication authentication);
}
