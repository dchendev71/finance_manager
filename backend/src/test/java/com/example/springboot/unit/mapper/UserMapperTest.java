package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.mapper.CurrencyMapperImpl;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.user.User;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import com.example.springboot.user.mapper.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
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

  private UserCreateRequest request;
  private Currency currency;
  private User user;

  @BeforeEach
  void setUp() {
    this.request = RequestTestFactory.User.register();
    this.currency = EntityTestFactory.CurrencyFactory.create();
    this.user = EntityTestFactory.UserFactory.create();
  }

  @Test
  @DisplayName("toEntity: should correctly map UserCreateRequest and Currency to User")
  void toEntity_shouldMapAllFields() {
    // When
    User user = userMapper.toEntity(request, currency);
    // Then
    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(TestConfig.User.email);
    assertThat(user.getPassword()).isEqualTo(TestConfig.User.password);
    assertThat(user.getCurrency()).isEqualTo(currency);
  }

  @Test
  @DisplayName("toEntity: should ignore createdAt, updatedAt, active")
  void toEntity_shouldIgnoreManagedFields() {
    // When
    User user = userMapper.toEntity(request, currency);

    // Then
    assertThat(user.getCreatedAt()).isNull();
    assertThat(user.getUpdatedAt()).isNull();
    assertThat(user.getActive()).isTrue(); // @Builder.Default
  }

  @Test
  @DisplayName("toResponse: should correctly map User to UserResponse")
  void toResponse_shouldMapAllFields() {
    // When
    UserResponse response = userMapper.toResponse(user);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.email()).isEqualTo(TestConfig.User.email);
    assertThat(response.currency()).isNotNull();
    assertThat(response.currency().code()).isEqualTo(TestConfig.Currency.code);
  }

  @Test
  @DisplayName("toResponse: should return null when User is null")
  void toResponse_shouldReturnNull_whenUserIsNull() {
    // When
    UserResponse response = userMapper.toResponse(null);
    // Then
    assertThat(response).isNull();
  }
}
