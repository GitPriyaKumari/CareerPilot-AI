package com.priya.careerpilotbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/user/profile")
    public String profile() {

        return "Welcome to CareerPilot AI";

    }

}