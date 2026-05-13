package com.example.springboot.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.helper.CurrencyTestFactory;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private CurrencyService currencyService;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  @Test
  @DisplayName("register: should create user successfully")
  void register_shouldCreateUser_whenValidRequest() {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    Currency currency = CurrencyTestFactory.createCurrency();
    User user = UserTestFactory.createUser();
    UserResponse expectedResponse = UserTestFactory.createUserResponse();

    when(currencyService.findByCode(CurrencyTestFactory.testCode)).thenReturn(currency);
    when(userMapper.toEntity(request, currency)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(expectedResponse);
    // When
    UserResponse response = userService.createNewUser(request);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.email()).isEqualTo(UserTestFactory.testEmail);
    verify(currencyService).findByCode(CurrencyTestFactory.testCode);
    verify(userRepository).save(user);
    verify(userMapper).toResponse(user);
  }

  @Test
  @DisplayName("register: should throw when currency not found")
  void register_shouldThrow_whenCurrencyNotFound() {
    // Given
    UserCreateRequest request =
        UserTestFactory.createUserRequest(
            UserTestFactory.testEmail, UserTestFactory.testPassword, "XYZ");
    when(currencyService.findByCode("XYZ"))
        .thenThrow(new RuntimeException("Currency not found: XYZ"));

    // When / Then
    assertThatThrownBy(() -> userService.createNewUser(request))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("XYZ");
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("createNewUser: should never expose password in response")
  void createNewUser_shouldNeverExposePassword() {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    Currency currency = CurrencyTestFactory.createCurrency();
    User user = UserTestFactory.createUser();
    UserResponse expectedResponse = UserTestFactory.createUserResponse();

    when(currencyService.findByCode(CurrencyTestFactory.testCode)).thenReturn(currency);
    when(userMapper.toEntity(request, currency)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(expectedResponse);

    // When
    UserResponse response = userService.createNewUser(request);

    // Then
    assertThat(response).hasNoNullFieldsOrPropertiesExcept("createdAt", "updatedAt");
  }
}
