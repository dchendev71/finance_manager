package com.example.springboot.balance;

import com.example.springboot.balance.dto.UserBalanceRequest;
import com.example.springboot.balance.dto.UserBalanceResponse;
import com.example.springboot.balance.mapper.UserBalanceMapper;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserBalanceController {
  private final UserBalanceService userBalanceService;
  private final UserBalanceMapper userBalanceMapper;

  @PostMapping(ApiRoutes.UserBalance.INCREASE)
  public ResponseEntity<UserBalanceResponse> increaseBalance(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody UserBalanceRequest request) {
    UserBalance userBalance = userBalanceService.increaseBalance(principal.getUsername(), request);
    return ResponseEntity.ok(userBalanceMapper.toResponse(userBalance));
  }

  @GetMapping(ApiRoutes.UserBalance.GET)
  public ResponseEntity<UserBalanceResponse> getBalance(
      @AuthenticationPrincipal CustomUserPrincipal principal) {
    UserBalance userBalance = userBalanceService.getBalance(principal.getUsername());
    return ResponseEntity.ok(userBalanceMapper.toResponse(userBalance));
  }
}
