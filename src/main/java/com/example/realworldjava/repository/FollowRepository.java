package com.example.realworldjava.repository;

import java.util.Optional;
import com.example.realworldjava.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowingIdAndFollowerId(Long followingId, Long followerId);
}
