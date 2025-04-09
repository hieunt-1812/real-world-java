package com.example.realworldjava.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Article {

    private String title;

    private String slug;

    private String description;

    private String body;

    private List<String> tagList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder.Default
    private int favoritesCount = 0;

    @Builder.Default
    private boolean favorited = false;

    private Profile author;

    @Data
    @AllArgsConstructor
    public static class MultiArticlesDto {

        List<Article> articles;

        long articlesCount;
    }

    @Data
    @AllArgsConstructor
    public static class SingleArticlesDto {
        Article article;
    }
}
