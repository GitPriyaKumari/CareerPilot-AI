package com.priya.careerpilotbackend.controller;

import com.priya.careerpilotbackend.dto.auth.LoginRequest;
import com.priya.careerpilotbackend.dto.auth.LoginResponse;
import com.priya.careerpilotbackend.dto.RefreshTokenRequest;
import com.priya.careerpilotbackend.dto.RefreshTokenResponse;
import com.priya.careerpilotbackend.dto.auth.RegisterRequest;
import com.priya.careerpilotbackend.dto.auth.RegisterResponse;
import com.priya.careerpilotbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        return authService.register(request);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);

    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return authService.refreshToken(request);

    }

    @DeleteMapping("/logout/{userId}")
    public String logout(@PathVariable Long userId) {

        authService.logout(userId);

        return "Logged out successfully.";

    }

}