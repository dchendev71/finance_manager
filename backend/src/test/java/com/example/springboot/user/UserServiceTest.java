package com.example.springboot.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.exception.EmailAlreadyExistsException;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.CurrencyService;
import com.example.springboot.currency.mapper.CurrencyMapper;
import com.example.springboot.currency.mapper.CurrencyMapperImpl;
import com.example.springboot.helper.CurrencyTestFactory;
import com.example.springboot.helper.UserTestFactory;
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
    this.userCreateRequest = UserTestFactory.createUserRequest();
    this.currency = CurrencyTestFactory.createCurrency();
  }

  @Test
  @DisplayName("register: should create user successfully")
  void register_shouldCreateUser_whenValidRequest() {
    // Given
    when(currencyService.findByCode(CurrencyTestFactory.testCode)).thenReturn(currency);
    // This is used because we use a real Mappers which will instantiate a real User, being
    // different
    // from the test, so we get back the instantiated User
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    // When
    UserResponse response = userService.registerNewUser(userCreateRequest);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.email()).isEqualTo(UserTestFactory.testEmail);
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
    when(currencyService.findByCode(CurrencyTestFactory.testCode)).thenReturn(currency);
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
    when(userRepository.findByEmail(UserTestFactory.testEmail))
        .thenReturn(Optional.of(UserTestFactory.createUser()));

    assertThatThrownBy(() -> userService.registerNewUser(userCreateRequest))
        .isInstanceOf(EmailAlreadyExistsException.class)
        .hasMessageContaining(UserTestFactory.testEmail);

    verify(userRepository, never()).save(any());
  }

  //
  @Test
  @DisplayName("changePassword: should change password")
  void changePassword_shouldChangePassword() {
    // Given
    User user = UserTestFactory.createUser();
    ChangePasswordRequest cpRequest =
        new ChangePasswordRequest(UserTestFactory.testPassword, "newPassword", "newPassword");

    when(userRepository.findByEmail(UserTestFactory.testEmail)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(cpRequest.currentPassword(), user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(cpRequest.newPassword())).thenReturn("encryptedNewPassword");
    // When
    userService.changePassword(UserTestFactory.testEmail, cpRequest);
    // Then
    verify(userRepository).findByEmail(UserTestFactory.testEmail);
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
    User user = UserTestFactory.createUser();
    ChangeEmailRequest ceRequest = new ChangeEmailRequest(UserTestFactory.testPassword, newEmail);
    // Stubbing dependencies
    when(userRepository.findByEmail(UserTestFactory.testEmail)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(ceRequest.currentPassword(), user.getPassword())).thenReturn(true);
    // When
    userService.changeEmail(UserTestFactory.testEmail, ceRequest);
    // Then
    verify(userRepository).findByEmail(UserTestFactory.testEmail);
    verify(passwordEncoder).matches(eq(ceRequest.currentPassword()), anyString());
    // Assert the managed entity's state was actually updated
    assertThat(user.getEmail()).isEqualTo(newEmail);
  }

  @Test
  @DisplayName("deleteUser: should successfully soft-delete user by setting active to false")
  void deleteUser_shouldSoftDeleteUser_whenUserExists() {
    // Given
    String email = UserTestFactory.testEmail;
    User user = UserTestFactory.createUser();
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
