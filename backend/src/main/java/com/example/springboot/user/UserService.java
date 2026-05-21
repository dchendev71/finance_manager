package com.example.springboot.user;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.InvalidCredentialsException;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
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
    // Check if user already exists
    if (userRepository.findByEmail(request.email()).isPresent()) {
      throw new ExistsException(User.class, request.email());
    }

    Currency currency = currencyService.findByCode(request.currencyCode());
    User user = userMapper.toEntity(request, currency);

    // Never store raw password
    String encodedPwd = passwordEncoder.encode(request.password());
    user.setPassword(encodedPwd);

    return userMapper.toResponse(userRepository.save(user));
  }

  @Transactional
  public UserResponse changeEmail(String currentEmail, ChangeEmailRequest changeEmailRequest) {
    // Check if email is the same
    if (currentEmail.equalsIgnoreCase(changeEmailRequest.newEmail())) {
      throw new ExistsException(User.class, currentEmail);
    }
    // Check if user still exists
    User user = userRepository.getByEmailOrThrow(currentEmail);
    // Verify that the passwords matches
    if (!passwordEncoder.matches(changeEmailRequest.currentPassword(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }
    // Check if email already taken
    if (userRepository.findByEmail(changeEmailRequest.newEmail()).isPresent()) {
      throw new ExistsException(User.class, changeEmailRequest.newEmail());
    }
    // Set the email
    user.setEmail(changeEmailRequest.newEmail());

    return userMapper.toResponse(userRepository.save(user));
  }

  @Transactional
  public UserResponse changePassword(String email, ChangePasswordRequest request) {
    // Check if user still exists
    User user = userRepository.getByEmailOrThrow(email);
    // If current password doesn't match
    if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }
    // Encode password
    user.setPassword(passwordEncoder.encode(request.newPassword()));

    return userMapper.toResponse(userRepository.save(user));
  }

  public UserResponse getProfile(Long id) {
    User user = userRepository.findById(id).get();
    System.out.println("GetProfile");
    return userMapper.toResponse(user);
  }

  @Transactional
  public void deleteUser(String email) {
    // Check if user still exists
    User user = userRepository.getByEmailOrThrow(email);
    user.setActive(false);

    userRepository.save(user);
  }
}
