package com.priya.careerpilotbackend.service;

import com.priya.careerpilotbackend.entity.RefreshToken;
import com.priya.careerpilotbackend.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void deleteByUser(Long userId);

}