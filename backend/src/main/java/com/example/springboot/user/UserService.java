package com.example.springboot.user;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CurrencyService currencyService;

  @Autowired
  public UserService(
      UserRepository userRepository, UserMapper userMapper, CurrencyService currencyService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.currencyService = currencyService;
  }

  @Transactional
  public UserResponse createNewUser(UserCreateRequest request) {
    Currency currency = currencyService.findByCode(request.currencyCode());
    User user = userMapper.toEntity(request, currency);

    return userMapper.toResponse(userRepository.save(user));
  }
}
