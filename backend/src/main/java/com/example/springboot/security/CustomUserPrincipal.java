package com.example.springboot.security;

import com.example.springboot.currency.Currency;
import com.example.springboot.user.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserPrincipal implements UserDetails, CredentialsContainer {
  private final long id;
  private final String email;
  private Collection<? extends GrantedAuthority> authorities;

  private String password;
  private final Currency currency;

  public CustomUserPrincipal(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.currency = user.getCurrency();

    this.password = user.getPassword();

    this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  public Long getId() {
    return this.id;
  }

  public Currency getCurrency() {
    return this.currency;
  }

  @Override
  public void eraseCredentials() {
    // Erase password after authentication
    this.password = null;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
