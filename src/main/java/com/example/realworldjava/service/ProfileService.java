package com.example.realworldjava.service;

import com.example.realworldjava.dto.ProfileDto;
import com.example.realworldjava.entity.UserEntity;
import com.example.realworldjava.exception.AppException;
import com.example.realworldjava.exception.Error;
import com.example.realworldjava.repository.FollowRepository;
import com.example.realworldjava.repository.UserRepository;
import com.example.realworldjava.entity.FollowEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public ProfileDto getProfile(String name) {
        UserEntity user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(
                Error.USER_NOT_FOUND));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUserEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(
                        Error.USER_NOT_FOUND
                ));
        boolean following = followRepository.findByFollowingIdAndFollowerId(user.getId(),
                currentUserEntity.getId()).isPresent();
        return ProfileDto.builder()
                .username(user.getUsername())
                .image(user.getImage())
                .bio(user.getBio())
                .following(following)
                .build();
    }

    public ProfileDto followUser(String name) {
        UserEntity followingUser = userRepository.findByUsername(name).orElseThrow(() -> new AppException(
                        Error.USER_NOT_FOUND));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity followerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(
                        Error.USER_NOT_FOUND
                ));
        followRepository.findByFollowingIdAndFollowerId(followingUser.getId(), followerUser.getId())
                .ifPresent(follow -> {
                    throw new AppException(Error.ALREADY_FOLLOWED_USER);
                });

        followRepository.save(new FollowEntity(followingUser, followerUser));
        return ProfileDto.builder()
                .username(followingUser.getUsername())
                .image(followingUser.getImage())
                .bio(followingUser.getBio())
                .following(true)
                .build();
    }

    public ProfileDto unfollowUser(String name) {
        UserEntity followingUser = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(
                        Error.USER_NOT_FOUND));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity followerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(
                        Error.USER_NOT_FOUND
                ));
        FollowEntity follow = followRepository.findByFollowingIdAndFollowerId(followingUser.getId(),
                        followerUser.getId())
                .orElseThrow(() -> new AppException(Error.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
        return ProfileDto.builder()
                .username(followingUser.getUsername())
                .image(followingUser.getImage())
                .bio(followingUser.getBio())
                .following(false)
                .build();
    }
}
