package com.example.springboot.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.currency.mapper.CurrencyMapper;
import com.example.springboot.currency.mapper.CurrencyMapperImpl;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import com.example.springboot.user.UserService;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapperImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private CurrencyService currencyService;

  private UserService userService;
  private UserCreateRequest userCreateRequest;
  private Currency currency;

  @BeforeEach
  void setUp() {
    CurrencyMapper currencyMapper = new CurrencyMapperImpl();
    UserMapperImpl userMapperImpl = new UserMapperImpl(currencyMapper);

    this.userService =
        new UserService(userRepository, userMapperImpl, currencyService, passwordEncoder);
    this.userCreateRequest = RequestTestFactory.User.register();
    this.currency = EntityTestFactory.CurrencyFactory.create();
  }

  @Test
  @DisplayName("register: should create user successfully")
  void register_shouldCreateUser_whenValidRequest() {
    // Given
    when(currencyService.findByCode(TestConfig.Currency.code)).thenReturn(currency);
    // This is used because we use a real Mappers which will instantiate a real User, being
    // different
    // from the test, so we get back the instantiated User
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    // When
    UserResponse response = userService.registerNewUser(userCreateRequest);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.email()).isEqualTo(TestConfig.User.email);
  }

  @Test
  @DisplayName("register: should throw when currency not found")
  void register_shouldThrow_whenCurrencyNotFound() {
    // Given
    UserCreateRequest request =
        RequestTestFactory.User.register(TestConfig.User.email, TestConfig.User.password, "XYZ");
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
    when(currencyService.findByCode(TestConfig.Currency.code)).thenReturn(currency);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    // When
    UserResponse response = userService.registerNewUser(userCreateRequest);
    // Then
    // Note: The id field is null because it is generated through Mockito
    assertThat(response).hasNoNullFieldsOrPropertiesExcept("createdAt", "updatedAt", "id");
  }

  @Test
  @DisplayName("registerNewUser: should not register same email")
  void registerNewUser_shouldNotRegisterSameEmail() {
    when(userRepository.findByEmail(TestConfig.User.email))
        .thenReturn(Optional.of(EntityTestFactory.UserFactory.create()));

    assertThatThrownBy(() -> userService.registerNewUser(userCreateRequest))
        .isInstanceOf(ExistsException.class)
        .hasMessageContaining(TestConfig.User.email);

    verify(userRepository, never()).save(any());
  }

  //
  @Test
  @DisplayName("changePassword: should change password")
  void changePassword_shouldChangePassword() {
    // Given
    User user = EntityTestFactory.UserFactory.create();
    ChangePasswordRequest cpRequest = RequestTestFactory.User.changePassword("newPassword");

    when(userRepository.findByEmail(TestConfig.User.email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(cpRequest.currentPassword(), user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(cpRequest.newPassword())).thenReturn("encryptedNewPassword");
    // When
    userService.changePassword(TestConfig.User.email, cpRequest);
    // Then
    verify(userRepository).findByEmail(TestConfig.User.email);
    // Note we can't use user.getPassword() because it is not encoded
    verify(passwordEncoder).matches(eq(cpRequest.currentPassword()), anyString());
    verify(passwordEncoder).encode(eq(cpRequest.newPassword()));
    verify(userRepository).save(user);
    // Verify that the user entity's password field was updated to the new encrypted string
    assertThat(user.getPassword()).isEqualTo("encryptedNewPassword");
  }

  @Test
  @DisplayName("changeEmail: should change email successfully")
  void changeEmail_shouldChangeEmail_whenValidRequest() {
    // Given
    String newEmail = "new@example.com";
    User user = EntityTestFactory.UserFactory.create();
    ChangeEmailRequest ceRequest = RequestTestFactory.User.changeEmail(newEmail);
    // Stubbing dependencies
    when(userRepository.findByEmail(TestConfig.User.email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(ceRequest.currentPassword(), user.getPassword())).thenReturn(true);
    // When
    userService.changeEmail(TestConfig.User.email, ceRequest);
    // Then
    verify(userRepository).findByEmail(TestConfig.User.email);
    verify(passwordEncoder).matches(eq(ceRequest.currentPassword()), anyString());
    // Assert the managed entity's state was actually updated
    assertThat(user.getEmail()).isEqualTo(newEmail);
  }

  @Test
  @DisplayName("deleteUser: should successfully soft-delete user by setting active to false")
  void deleteUser_shouldSoftDeleteUser_whenUserExists() {
    // Given
    String email = TestConfig.User.email;
    User user = EntityTestFactory.UserFactory.create();
    user.setActive(true); // Ensure initial state is explicitly active

    // Stubbing dependencies
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    userService.deleteUser(email);

    // Then
    verify(userRepository).findByEmail(email);
    verify(userRepository).save(user);

    // Assert that the entity state was altered to false (Soft-Deletion verification)
    assertThat(user.getActive()).isFalse();
  }
}
