package com.example.springboot.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PortfolioCreateRequest(
    @NotBlank(message = "Specify the portfolio name") String portfolioName) {}
