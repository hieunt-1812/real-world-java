package com.example.realworldjava.service;

import com.example.realworldjava.dto.ArticleDto;
import com.example.realworldjava.entity.FavoriteEntity;
import com.example.realworldjava.model.Article;
import com.example.realworldjava.model.Article.MultiArticlesDto;
import com.example.realworldjava.model.Article.SingleArticlesDto;
import com.example.realworldjava.model.Profile;
import com.example.realworldjava.dto.UpdateArticleDto;
import com.example.realworldjava.entity.ArticleEntity;
import com.example.realworldjava.entity.TagEntity;
import com.example.realworldjava.entity.UserEntity;
import com.example.realworldjava.exception.AppException;
import com.example.realworldjava.exception.Error;
import com.example.realworldjava.repository.ArticleRepository;
import com.example.realworldjava.repository.FavoriteRepository;
import com.example.realworldjava.repository.FollowRepository;
import com.example.realworldjava.repository.TagRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public SingleArticlesDto createArticle(ArticleDto articleDto) {
        String slug =
                String.join("-", articleDto.getTitle().split(" ")) + "-" + UUID.randomUUID()
                        .toString().substring(0, 8);
        UserEntity currentUser = userService.getCurrentUserEntity();
        ArticleEntity articleEntity = new ArticleEntity(slug, articleDto.getTitle(),
                articleDto.getDescription(), articleDto.getBody(), currentUser);
        articleEntity.setTagList(
                articleDto.getTagList().stream().map(tag -> new TagEntity(tag, articleEntity))
                        .toList());
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);
        Profile profile = Profile.builder()
                .username(currentUser.getUsername())
                .bio(currentUser.getBio())
                .image(currentUser.getImage()).build();
        return new SingleArticlesDto(Article.builder()
                .title(savedArticleEntity.getTitle())
                .slug(savedArticleEntity.getSlug())
                .description(savedArticleEntity.getDescription())
                .body(savedArticleEntity.getBody())
                .createdAt(savedArticleEntity.getCreatedAt())
                .updatedAt(savedArticleEntity.getUpdatedAt())
                .author(profile)
                .tagList(savedArticleEntity.getTagList().stream().map(TagEntity::getTag).toList())
                .build()
        );
    }

    public SingleArticlesDto getArticle(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        UserEntity currentUser = userService.getCurrentUserEntity();
        boolean following = followRepository.findByFollowingIdAndFollowedById(articleEntity.getId(),
                currentUser.getId()).isPresent();
        Profile profile = (Profile.builder()
                .username(articleEntity.getAuthor().getUsername())
                .bio(articleEntity.getAuthor().getBio()).image(articleEntity.getAuthor().getImage())
                .following(following).build());
        return new SingleArticlesDto(Article.builder()
                .title(articleEntity.getTitle())
                .slug(articleEntity.getSlug())
                .description(articleEntity.getDescription())
                .body(articleEntity.getBody())
                .createdAt(articleEntity.getCreatedAt())
                .updatedAt(articleEntity.getUpdatedAt())
                .author(profile)
                .tagList(articleEntity.getTagList().stream().map(TagEntity::getTag).toList())
                .build()
        );
    }

    @Transactional
    public SingleArticlesDto updateArticle(String slug,
                                              UpdateArticleDto updateArticleDto) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        Optional.ofNullable(updateArticleDto.getTitle()).filter(title -> !title.isEmpty())
                .ifPresent(title -> {
                    String newSlug =
                            String.join("-", updateArticleDto.getTitle().split(" ")) + "-"
                                    + UUID.randomUUID().toString().substring(0, 8);
                    articleEntity.setSlug(newSlug);
                    articleEntity.setTitle(title);
                });
        Optional.ofNullable(updateArticleDto.getDescription())
                .filter(description -> !description.isEmpty())
                .ifPresent(articleEntity::setDescription);
        Optional.ofNullable(updateArticleDto.getBody()).filter(body -> !body.isEmpty())
                .ifPresent(articleEntity::setBody);
        ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);
        UserEntity currentUser = userService.getCurrentUserEntity();
        boolean following = followRepository.findByFollowingIdAndFollowedById(
                articleEntity.getAuthor().getId(), currentUser.getId()).isPresent();
        Profile profile = Profile.builder()
                .username(savedArticleEntity.getAuthor().getUsername())
                .bio(savedArticleEntity.getAuthor().getBio())
                .image(savedArticleEntity.getAuthor().getImage())
                .following(following).build();
        return new SingleArticlesDto(
                Article.builder()
                        .title(savedArticleEntity.getTitle())
                        .slug(savedArticleEntity.getSlug())
                        .description(savedArticleEntity.getDescription())
                        .body(savedArticleEntity.getBody())
                        .createdAt(savedArticleEntity.getCreatedAt())
                        .updatedAt(savedArticleEntity.getUpdatedAt())
                        .author(profile)
                        .tagList(savedArticleEntity.getTagList().stream().map(TagEntity::getTag)
                                .toList())
                        .build()
        );
    }

    @Transactional
    public void deleteArticle(String slug) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        UserEntity currentUser = userService.getCurrentUserEntity();
        if (!articleEntity.getAuthor().getId().equals(currentUser.getId())) {
            throw new AppException(Error.ARTICLE_DELETE_FORBIDDEN);
        }
        articleRepository.delete(articleEntity);
    }

    public MultiArticlesDto getArticlesByTag(String tag, String author, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        final List<Long> followingIds = new ArrayList<>();
        try {
            UserEntity currentUser = userService.getCurrentUserEntity();
            followingIds.addAll(followRepository.findByFollowedById(currentUser.getId()).stream()
                    .map(e -> e.getFollowing().getId()).toList());
        } catch (AppException ignored) {
        }

        Page<ArticleEntity> articleEntities = articleRepository.findArticles(tag, author, pageable);
        return new MultiArticlesDto(articleEntities.stream()
                .map(e -> Article.builder()
                        .title(e.getTitle())
                        .slug(e.getSlug())
                        .description(e.getDescription())
                        .body(e.getBody())
                        .tagList(e.getTagList().stream().map(TagEntity::getTag).toList())
                        .createdAt(e.getCreatedAt())
                        .updatedAt(e.getUpdatedAt())
                        .author(Profile.builder()
                                .username(e.getAuthor().getUsername())
                                .image(e.getAuthor().getImage())
                                .bio(e.getAuthor().getBio())
                                .following(followingIds.contains(e.getAuthor().getId())).build())
                        .build()
                ).toList(),
                articleEntities.getTotalElements());
    }

    public MultiArticlesDto getFeedArticles(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        UserEntity currentUser = userService.getCurrentUserEntity();
        List<Long> followingIds = followRepository.findByFollowedById(currentUser.getId()).stream()
                .map(e -> e.getFollowing().getId()).toList();
        Page<ArticleEntity> articleEntities = articleRepository.findByAuthorIds(followingIds,
                pageable);
        return new MultiArticlesDto(articleEntities.stream()
                .map(e -> Article.builder()
                        .title(e.getTitle())
                        .slug(e.getSlug())
                        .description(e.getDescription())
                        .body(e.getBody())
                        .tagList(e.getTagList().stream().map(TagEntity::getTag).toList())
                        .createdAt(e.getCreatedAt())
                        .updatedAt(e.getUpdatedAt())
                        .author(Profile.builder()
                                .username(e.getAuthor().getUsername())
                                .image(e.getAuthor().getImage())
                                .bio(e.getAuthor().getBio())
                                .following(followingIds.contains(e.getAuthor().getId())).build())
                        .build()
                ).toList(),
                articleEntities.getTotalElements());
    }

    private SingleArticlesDto convertToSingleArticleDto(ArticleEntity article, UserEntity currentUser) {
        boolean following = followRepository.findByFollowingIdAndFollowedById(
                article.getAuthor().getId(), currentUser.getId()).isPresent();
        boolean favorited = favoriteRepository.findByUserAndArticle(currentUser, article).isPresent();
        int favoritesCount = favoriteRepository.countByArticle(article);

        return new SingleArticlesDto(Article.builder()
                .title(article.getTitle())
                .slug(article.getSlug())
                .description(article.getDescription())
                .body(article.getBody())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .tagList(article.getTagList().stream().map(TagEntity::getTag).toList())
                .author(Profile.builder()
                        .username(article.getAuthor().getUsername())
                        .bio(article.getAuthor().getBio())
                        .image(article.getAuthor().getImage())
                        .following(following)
                        .build())
                .favorited(favorited)
                .favoritesCount(favoritesCount)
                .build());
    }

    @Transactional
    public SingleArticlesDto addFavorite(String slug) {
        ArticleEntity article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        UserEntity currentUser = userService.getCurrentUserEntity();
        if (favoriteRepository.findByUserAndArticle(currentUser, article).isEmpty()) {
            favoriteRepository.save(new FavoriteEntity(null, currentUser, article));
        }
        return convertToSingleArticleDto(article, currentUser);
    }

    @Transactional
    public SingleArticlesDto deleteFavorite(String slug) {
        ArticleEntity article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        UserEntity currentUser = userService.getCurrentUserEntity();
        favoriteRepository.findByUserAndArticle(currentUser, article)
                .ifPresent(favoriteRepository::delete);
        return convertToSingleArticleDto(article, currentUser);
    }

    public List<String> getTags() {
        return tagRepository.findAllTags();
    }
}
