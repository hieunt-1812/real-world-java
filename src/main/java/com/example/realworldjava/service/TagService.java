package com.example.realworldjava.service;

import com.example.realworldjava.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<String> getAllTags() {
        return tagRepository.findAllTags();
    }
}
