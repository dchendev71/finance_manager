package com.example.springboot.balance.mapper;

import com.example.springboot.balance.UserBalance;
import com.example.springboot.balance.dto.UserBalanceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserBalanceMapper {
  UserBalanceResponse toResponse(UserBalance userBalance);
}
