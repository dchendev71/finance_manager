package com.example.springboot.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.exception.EmailAlreadyExistsException;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.helper.CurrencyTestFactory;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;

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
    UserResponse response = userService.registerNewUser(request);
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
    assertThatThrownBy(() -> userService.registerNewUser(request))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("XYZ");
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("registerNewUser: should never expose password in response")
  void registerNewUser_shouldNeverExposePassword() {
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
    UserResponse response = userService.registerNewUser(request);

    // Then
    assertThat(response).hasNoNullFieldsOrPropertiesExcept("createdAt", "updatedAt");
  }

  @Test
  @DisplayName("registerNewUser: should not register same email")
  void registerNewUser_shouldNotRegisterSameEmail() {
    UserCreateRequest request = UserTestFactory.createUserRequest();
    User user = UserTestFactory.createUser();

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> userService.registerNewUser(request))
        .isInstanceOf(EmailAlreadyExistsException.class)
        .hasMessageContaining(request.email());

    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("changePassword: should change password")
  void changePassword_shouldChangePassword() {
    // Given
    String email = UserTestFactory.testEmail;
    User user = UserTestFactory.createUser();
    ChangePasswordRequest cpRequest =
        new ChangePasswordRequest(UserTestFactory.testPassword, "newPassword", "newPassword");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(cpRequest.currentPassword(), user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(cpRequest.newPassword())).thenReturn("encryptedNewPassword");

    // When
    userService.changePassword(email, cpRequest);

    // Then
    verify(userRepository).findByEmail(email);
    verify(passwordEncoder).matches(anyString(), eq(cpRequest.currentPassword()));
    verify(passwordEncoder).encode(eq(cpRequest.newPassword()));

    // Verify that the user entity's password field was updated to the new encrypted string
    assertThat(user.getPassword()).isEqualTo("encryptedNewPassword");
  }

  @Test
  @DisplayName("changeEmail: should change email successfully")
  void changeEmail_shouldChangeEmail_whenValidRequest() {
    // Given
    String currentEmail = UserTestFactory.testEmail;
    String newEmail = "new@example.com";
    User user = UserTestFactory.createUser();
    ChangeEmailRequest ceRequest = new ChangeEmailRequest(UserTestFactory.testPassword, newEmail);

    // Stubbing dependencies
    when(userRepository.findByEmail(currentEmail)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(ceRequest.currentPassword(), user.getPassword())).thenReturn(true);
    // When
    userService.changeEmail(currentEmail, ceRequest);
    // Then
    verify(userRepository).findByEmail(currentEmail);
    verify(passwordEncoder).matches(eq(ceRequest.currentPassword()), anyString());

    // Assert the managed entity's state was actually updated
    assertThat(user.getEmail()).isEqualTo(newEmail);
  }
}
