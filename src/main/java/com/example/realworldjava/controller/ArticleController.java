package com.example.realworldjava.controller;

import com.example.realworldjava.dto.ArticleDto;
import com.example.realworldjava.model.Article;
import com.example.realworldjava.model.Article.MultiArticlesDto;
import com.example.realworldjava.model.Article.SingleArticlesDto;
import com.example.realworldjava.model.Comment;
import com.example.realworldjava.dto.UpdateArticleDto;
import com.example.realworldjava.dto.CommentDto;
import com.example.realworldjava.service.ArticleService;
import com.example.realworldjava.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<SingleArticlesDto> createArticle(
            @Valid @RequestBody ArticleDto articleReqDto) {
        return ResponseEntity.ok(articleService.createArticle(articleReqDto));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<SingleArticlesDto> getArticle(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getArticle(slug));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<SingleArticlesDto> getArticle(
            @PathVariable String slug,
            @Valid @RequestBody UpdateArticleDto updateArticleReqDto) {
        return ResponseEntity.ok(articleService.updateArticle(slug, updateArticleReqDto));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<ArticleDto> deleteArticle(@PathVariable String slug) {
        articleService.deleteArticle(slug);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MultiArticlesDto> getArticlesByTag(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(articleService.getArticlesByTag(tag, author, offset, limit));
    }

    @GetMapping("/feed")
    public ResponseEntity<MultiArticlesDto> getFeedArticles(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(articleService.getFeedArticles(offset, limit));
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<Comment.SingleComment> createCommentsToAnArticle(@PathVariable String slug, @RequestBody @Valid CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createCommentsToAnArticle(slug, commentDto));
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<Comment.MultipleComments> getCommentsToAnArticle(@PathVariable String slug) {
        return ResponseEntity.ok(commentService.getCommentsToAnArticle(slug));
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("slug") String slug, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(slug, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticlesDto> favoriteArticle(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.addFavorite(slug));
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticlesDto> unfavoriteArticle(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.deleteFavorite(slug));
    }

    @GetMapping("/tags")
    public ResponseEntity<Map<String, List<String>>> getTags() {
        return ResponseEntity.ok(Map.of("tags", articleService.getTags()));
    }
}
