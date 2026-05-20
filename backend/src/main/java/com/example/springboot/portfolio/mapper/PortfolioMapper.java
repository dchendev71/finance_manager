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
  Portfolio toEntity(User user, String name);

  @Mapping(target = "userResponse", source = "portfolio.user")
  @Mapping(target = "portfolioName", source = "portfolio.name")
  PortfolioResponse toResponse(Portfolio portfolio);
}
