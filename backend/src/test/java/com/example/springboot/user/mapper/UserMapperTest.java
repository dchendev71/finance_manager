package com.example.springboot.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.mapper.CurrencyMapperImpl;
import com.example.springboot.helper.CurrencyTestFactory;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.User;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, CurrencyMapperImpl.class})
class UserMapperTest {

  @Autowired private UserMapper userMapper;

  @Test
  @DisplayName("toEntity: should correctly map UserCreateRequest and Currency to User")
  void toEntity_shouldMapAllFields() {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    Currency currency = CurrencyTestFactory.createCurrency();
    // When
    User user = userMapper.toEntity(request, currency);
    // Then
    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(UserTestFactory.testEmail);
    assertThat(user.getPassword()).isEqualTo(UserTestFactory.testPassword);
    assertThat(user.getCurrency()).isEqualTo(currency);
  }

  @Test
  @DisplayName("toEntity: should ignore id, createdAt, updatedAt, active")
  void toEntity_shouldIgnoreManagedFields() {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    Currency currency = CurrencyTestFactory.createCurrency();
    // When
    User user = userMapper.toEntity(request, currency);

    // Then
    assertThat(user.getId()).isNull();
    assertThat(user.getCreatedAt()).isNull();
    assertThat(user.getUpdatedAt()).isNull();
    assertThat(user.getActive()).isTrue(); // @Builder.Default
  }

  @Test
  @DisplayName("toResponse: should correctly map User to UserResponse")
  void toResponse_shouldMapAllFields() {
    // Given
    Currency currency = CurrencyTestFactory.createCurrency();
    User user = UserTestFactory.createUser();
    // When
    UserResponse response = userMapper.toResponse(user);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(UserTestFactory.testId);
    assertThat(response.email()).isEqualTo(UserTestFactory.testEmail);
    assertThat(response.currency()).isNotNull();
    assertThat(response.currency().code()).isEqualTo(UserTestFactory.testCurrencyCode);
  }

  @Test
  @DisplayName("toResponse: should return null when User is null")
  void toResponse_shouldReturnNull_whenUserIsNull() {
    // When
    UserResponse response = userMapper.toResponse(null);
    // Then
    assertThat(response).isNull();
  }

  @Test
  @DisplayName("toResponse: should not expose password")
  void toResponse_shouldNotExposePassword() {
    // Given
    User user = UserTestFactory.createUser();
    // When
    UserResponse response = userMapper.toResponse(user);
    // Then — UserResponse has no password field
    assertThat(response).hasNoNullFieldsOrPropertiesExcept("createdAt", "updatedAt");
  }
}
