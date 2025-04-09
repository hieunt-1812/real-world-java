package com.example.realworldjava.controller;

import com.example.realworldjava.dto.ProfileDto;
import com.example.realworldjava.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // POST /profiles/{username}/follow
    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileDto> followUser(@PathVariable("username") String username) {
        ProfileDto profile = profileService.followUser(username);
        return ResponseEntity.ok(profile);
    }

    // GET /profiles/{username}
    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getByUsername(@PathVariable String username) {
        ProfileDto profile = profileService.getProfile(username);
        return ResponseEntity.ok(profile);
    }

    // DELETE /profiles/{username}/follow
    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileDto> unfollowUser(@PathVariable("username") String username) {
        ProfileDto profile = profileService.unfollowUser(username);
        return ResponseEntity.ok(profile);
    }
}