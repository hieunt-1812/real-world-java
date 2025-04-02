package com.example.realworldjava.service;

import com.example.realworldjava.dto.AuthenticationDto;
import com.example.realworldjava.dto.RegistrationDto;
import com.example.realworldjava.dto.UserDto;
import com.example.realworldjava.entity.UserEntity;
import com.example.realworldjava.exception.AppException;
import com.example.realworldjava.exception.Error;
import com.example.realworldjava.model.User;
import com.example.realworldjava.repository.UserRepository;
import com.example.realworldjava.security.JwtUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(RegistrationDto registrationReqDto) {
        Optional.ofNullable(registrationReqDto.getEmail())
                .filter(userRepository::existsByEmail)
                .ifPresent(email -> {
                    throw new AppException(Error.EMAIL_TAKEN);
                });
        Optional.ofNullable(registrationReqDto.getEmail())
                .filter(userRepository::existsByUsername)
                .ifPresent(username -> {
                    throw new AppException(Error.USERNAME_TAKEN);
                });
        UserEntity userEntity = userRepository.save(new UserEntity(
                registrationReqDto.getEmail(),
                registrationReqDto.getUsername(),
                passwordEncoder.encode(registrationReqDto.getPassword())
        ));
        return User.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .image(userEntity.getImage())
                .bio(userEntity.getBio())
                .demo(userEntity.getDemo())
                .token(jwtUtil.generateToken(userEntity.getEmail())).build();
    }

    public User loginUser(AuthenticationDto loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword())
            );
        } catch (Exception e) {
            throw new AppException(Error.INVALID_LOGIN);
        }
        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        return User.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .image(userEntity.getImage())
                .bio(userEntity.getBio())
                .demo(userEntity.getDemo())
                .token(jwtUtil.generateToken(userEntity.getEmail())).build();
    }

    public User getCurrentUser() {
        UserEntity userEntity = getCurrentUserEntity();
        return User.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .image(userEntity.getImage())
                .bio(userEntity.getBio())
                .demo(userEntity.getDemo())
                .token(jwtUtil.generateToken(userEntity.getEmail())).build();
    }
    
    public User updateCurrentUser(UserDto userDto) {
        Optional.ofNullable(userDto.getEmail())
                .filter(userRepository::existsByEmail)
                .ifPresent(email -> {
                    throw new AppException(Error.EMAIL_TAKEN);
                });
        Optional.ofNullable(userDto.getUsername())
                .filter(userRepository::existsByUsername)
                .ifPresent(username -> {
                    throw new AppException(Error.USERNAME_TAKEN);
                });
        UserEntity currentUser = updateUserEntity(userDto);
        UserEntity userEntity = userRepository.save(currentUser);
        return User.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .image(userEntity.getImage())
                .bio(userEntity.getBio())
                .demo(userEntity.getDemo())
                .token(jwtUtil.generateToken(userEntity.getEmail())).build();
    }

    private UserEntity updateUserEntity(UserDto userDto) {
        UserEntity currentUser = getCurrentUserEntity();
        Optional.ofNullable(userDto.getEmail()).filter(email -> !email.isEmpty())
                .ifPresent(currentUser::setEmail);
        Optional.ofNullable(userDto.getUsername()).filter(username -> !username.isEmpty())
                .ifPresent(currentUser::setUsername);
        Optional.ofNullable(userDto.getBio()).filter(bio -> !bio.isEmpty())
                .ifPresent(currentUser::setBio);
        Optional.ofNullable(userDto.getDemo()).ifPresent(currentUser::setDemo);
        Optional.ofNullable(userDto.getImage()).filter(image -> !image.isEmpty())
                .ifPresent(currentUser::setImage);
        return currentUser;
    }

    private UserEntity getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
    }
}

