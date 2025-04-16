package com.example.realworldjava.controller;

import com.example.realworldjava.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTags() {
        return ResponseEntity.ok(Map.of("tags", tagService.getAllTags()));
    }
}
