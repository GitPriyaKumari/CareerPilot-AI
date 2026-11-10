package com.priya.careerpilotbackend.service.impl;

import com.priya.careerpilotbackend.dto.*;
import com.priya.careerpilotbackend.dto.auth.LoginRequest;
import com.priya.careerpilotbackend.dto.auth.LoginResponse;
import com.priya.careerpilotbackend.dto.auth.RegisterRequest;
import com.priya.careerpilotbackend.dto.auth.RegisterResponse;
import com.priya.careerpilotbackend.entity.RefreshToken;
import com.priya.careerpilotbackend.entity.User;
import com.priya.careerpilotbackend.repository.UserRepository;
import com.priya.careerpilotbackend.security.jwt.JwtService;
import com.priya.careerpilotbackend.service.AuthService;
import com.priya.careerpilotbackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return new RegisterResponse("User Registered Successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        try {

            System.out.println("========== LOGIN ==========");
            System.out.println("Email = " + request.getEmail());
            System.out.println("Password = " + request.getPassword());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            System.out.println("Authentication Successful");

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String accessToken = jwtService.generateToken(user.getEmail());

            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(user);

            return new LoginResponse(
                    accessToken,
                    refreshToken.getToken(),
                    "Bearer"
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService
                .verifyRefreshToken(request.getRefreshToken());

        String accessToken = jwtService.generateToken(
                refreshToken.getUser().getEmail()
        );

        return new RefreshTokenResponse(
                accessToken,
                "Bearer"
        );
    }

    @Override
    public void logout(Long userId) {

        refreshTokenService.deleteByUser(userId);

    }
}