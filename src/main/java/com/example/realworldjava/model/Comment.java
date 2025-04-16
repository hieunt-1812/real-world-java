package com.example.realworldjava.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Comment {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body;

    private Profile author;

    // Nested static class: wrapper for one comment
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SingleComment {
        Comment comment;
    }

    // Nested static class: wrapper for many comment
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MultipleComments {
        List<Comment> comments;
    }
}
