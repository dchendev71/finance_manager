package com.example.springboot.balance;

import com.example.springboot.balance.dto.UserBalanceRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserBalanceController {
  private final UserBalanceService userBalanceService;

  @PostMapping(ApiRoutes.UserBalance.INCREASE)
  public ResponseEntity<Void> increaseBalance(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody UserBalanceRequest request) {
    userBalanceService.increaseBalance(principal.getUsername(), request);
    return ResponseEntity.noContent().build();
  }
}
