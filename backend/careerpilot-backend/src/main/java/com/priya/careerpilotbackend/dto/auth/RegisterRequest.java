package com.priya.careerpilotbackend.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
}