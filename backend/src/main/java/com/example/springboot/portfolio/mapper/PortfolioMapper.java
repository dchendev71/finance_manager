package com.example.springboot.portfolio.mapper;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.user.User;
import com.example.springboot.user.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PortfolioMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "name", source = "portfolioName")
  @Mapping(target = "user", source = "user")
  Portfolio toEntity(User user, String portfolioName);

  PortfolioResponse toResponse(Portfolio portfolio);
}
