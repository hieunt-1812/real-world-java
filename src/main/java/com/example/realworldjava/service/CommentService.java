package com.example.realworldjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.example.realworldjava.dto.CommentDto;
import com.example.realworldjava.model.Comment;
import com.example.realworldjava.model.Profile;
import com.example.realworldjava.entity.ArticleEntity;
import com.example.realworldjava.entity.CommentEntity;
import com.example.realworldjava.entity.UserEntity;
import com.example.realworldjava.exception.AppException;
import com.example.realworldjava.exception.Error;
import com.example.realworldjava.repository.ArticleRepository;
import com.example.realworldjava.repository.CommentRepository;
import com.example.realworldjava.repository.FollowRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    public Comment.SingleComment createCommentsToAnArticle(String slug,
                                                                 CommentDto commentDto) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        UserEntity currentUser = userService.getCurrentUserEntity();
        CommentEntity commentEntity = commentRepository.save(
                new CommentEntity(commentDto.getBody(), currentUser, articleEntity));
        return new Comment.SingleComment(
                Comment.builder()
                        .id(commentEntity.getId())
                        .body(commentEntity.getBody())
                        .createdAt(commentEntity.getCreatedAt())
                        .updatedAt(commentEntity.getUpdatedAt())
                        .author(Profile.builder()
                                .username(currentUser.getUsername())
                                .bio(currentUser.getBio())
                                .image(currentUser.getImage())
                                .following(false).build())
                        .build()
        );
    }

    public Comment.MultipleComments getCommentsToAnArticle(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        List<CommentEntity> commentEntities = commentRepository.findByArticleId(
                articleEntity.getId());
        final List<Long> followingIds = new ArrayList<>();
        try {
            UserEntity currentUser = userService.getCurrentUserEntity();
            followingIds.addAll(followRepository.findByFollowedById(currentUser.getId()).stream()
                    .map(e -> e.getFollowing().getId()).toList());
        } catch (AppException ignored) {
        }
        return new Comment.MultipleComments(
                commentEntities.stream().map(commentEntity -> Comment.builder()
                        .id(commentEntity.getId())
                        .body(commentEntity.getBody())
                        .createdAt(commentEntity.getCreatedAt())
                        .updatedAt(commentEntity.getUpdatedAt())
                        .author(Profile.builder()
                                .username(articleEntity.getAuthor().getUsername())
                                .bio(articleEntity.getAuthor().getBio())
                                .image(articleEntity.getAuthor().getImage())
                                .following(followingIds.contains(articleEntity.getId())).build())
                        .build()).toList()
        );
    }

    public void deleteComment(String slug, Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .filter(e -> e.getArticle().getSlug().equals(slug))
                .orElseThrow(() -> new AppException(Error.COMMENT_NOT_FOUND));
        commentRepository.delete(commentEntity);
    }
}
