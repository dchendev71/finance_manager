package com.example.springboot.user.mapper;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.mapper.CurrencyMapper;
import com.example.springboot.user.User;
import com.example.springboot.user.dto.UserResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {CurrencyMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "email", source = "request.email")
  @Mapping(target = "password", source = "request.password")
  @Mapping(target = "currency", source = "currency")
  User toEntity(UserCreateRequest request, Currency currency);

  UserResponse toResponse(User user);
}
