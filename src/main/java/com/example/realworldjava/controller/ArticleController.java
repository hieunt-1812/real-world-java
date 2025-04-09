package com.example.realworldjava.controller;

import com.example.realworldjava.dto.ArticleDto;
import com.example.realworldjava.model.Article;
import com.example.realworldjava.model.Article.MultiArticlesDto;
import com.example.realworldjava.model.Article.SingleArticlesDto;
import com.example.realworldjava.dto.UpdateArticleDto;
import com.example.realworldjava.service.ArticleService;
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

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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
}
