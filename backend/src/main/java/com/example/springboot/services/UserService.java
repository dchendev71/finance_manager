package com.example.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.entities.User;
import com.example.springboot.repositories.UserRepository;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerNewUser(String email, String password) {

        User user = User.builder()
                        .email(email)
                        .password(password)
                        .build();

        return userRepository.save(user);
    }
}
