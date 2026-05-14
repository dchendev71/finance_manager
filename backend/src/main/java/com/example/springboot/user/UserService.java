package com.example.springboot.user;

import com.example.springboot.common.exception.EmailAlreadyExistsException;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final CurrencyService currencyService;

  @Autowired
  public UserService(
      UserRepository userRepository,
      UserMapper userMapper,
      CurrencyService currencyService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.currencyService = currencyService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public UserResponse registerNewUser(UserCreateRequest request) {
    Currency currency = currencyService.findByCode(request.currencyCode());
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new EmailAlreadyExistsException(request.email());
    }
    User user = userMapper.toEntity(request, currency);

    String encodedPwd = passwordEncoder.encode(request.password());
    user.setPassword(encodedPwd);

    return userMapper.toResponse(userRepository.save(user));
  }
}
