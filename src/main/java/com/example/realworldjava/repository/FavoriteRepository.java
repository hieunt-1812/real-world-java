package com.example.realworldjava.repository;

import com.example.realworldjava.entity.ArticleEntity;
import com.example.realworldjava.entity.FavoriteEntity;
import java.util.Optional;

import com.example.realworldjava.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByUserAndArticle(UserEntity user, ArticleEntity article);
    int countByArticle(ArticleEntity article);
}
