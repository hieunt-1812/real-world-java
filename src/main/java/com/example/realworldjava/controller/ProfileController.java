package com.example.realworldjava.controller;

import com.example.realworldjava.dto.ProfileDto;
import com.example.realworldjava.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/{username}/follow")
    public ProfileDto followUser(@PathVariable("username") String name) {
        return profileService.followUser(name);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfile(username));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto unfollowUser(@PathVariable("username") String name) {
        return profileService.unfollowUser(name);
    }
}

