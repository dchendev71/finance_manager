package com.example.springboot.security;

import com.example.springboot.user.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserPrincipal implements UserDetails, CredentialsContainer {
  private Collection<? extends GrantedAuthority> authorities;

  private final User user;

  public CustomUserPrincipal(User user) {
    this.user = user;
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  public User getUser() {
    return user;
  }

  @Override
  public void eraseCredentials() {
    // Erase password after authentication
    user.setPassword(null);
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
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
