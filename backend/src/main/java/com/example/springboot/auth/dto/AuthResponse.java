package com.example.springboot.auth.dto;

import com.example.springboot.user.dto.UserResponse;

public record AuthResponse(String jwtToken, UserResponse userResponse) {}
