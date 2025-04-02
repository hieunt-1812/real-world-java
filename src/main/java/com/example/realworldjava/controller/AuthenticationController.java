package com.example.realworldjava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.realworldjava.dto.AuthenticationDto;
import com.example.realworldjava.dto.RegistrationDto;
import com.example.realworldjava.dto.UserDto;
import com.example.realworldjava.model.User;
import com.example.realworldjava.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<User> registerUser(
            @Valid @RequestBody RegistrationDto registrationDto) {
        User userResponse = userService.registerUser(registrationDto);
        return ResponseEntity.status(201).body(userResponse);
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody AuthenticationDto loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateCurrentUser(
            @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateCurrentUser(userDto));
    }
}
