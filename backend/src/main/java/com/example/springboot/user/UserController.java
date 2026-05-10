package com.example.springboot.controllers;

import com.example.springboot.entities.User;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<User> addNewUser(
      @RequestParam String email, @RequestParam String password) {
    User user = userService.registerNewUser(email, password);
    return ResponseEntity.ok(user);
  }
}
