package com.example.springboot.portfolio.dto;

import com.example.springboot.user.dto.UserResponse;
import lombok.Builder;

@Builder
public record PortfolioResponse(String portfolioName, UserResponse userResponse) {}
