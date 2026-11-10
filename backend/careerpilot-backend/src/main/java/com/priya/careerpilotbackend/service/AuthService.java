package com.priya.careerpilotbackend.service;

import com.priya.careerpilotbackend.dto.*;
import com.priya.careerpilotbackend.dto.auth.LoginRequest;
import com.priya.careerpilotbackend.dto.auth.LoginResponse;
import com.priya.careerpilotbackend.dto.auth.RegisterRequest;
import com.priya.careerpilotbackend.dto.auth.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(Long userId);

}