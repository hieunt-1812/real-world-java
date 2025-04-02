package com.example.realworldjava.service;

import com.example.realworldjava.exception.AppException;
import com.example.realworldjava.exception.Error;
import com.example.realworldjava.entity.UserEntity;
import com.example.realworldjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        return User.withUsername(userEntity.getEmail()).password(userEntity.getPassword())
                .build();
    }
}

