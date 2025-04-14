package com.example.realworldjava.repository;

import java.util.Optional;
import com.example.realworldjava.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowingIdAndFollowedById(Long followingId, Long followedById);

    List<FollowEntity> findByFollowedById(Long followedById);
}
