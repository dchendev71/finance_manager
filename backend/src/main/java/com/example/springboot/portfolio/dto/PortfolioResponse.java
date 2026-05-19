package com.example.springboot.portfolio.dto;

import com.example.springboot.user.User;

public record PortfolioResponse(String portfolioName, User user) {}
