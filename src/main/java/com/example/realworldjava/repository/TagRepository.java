package com.example.realworldjava.repository;

import com.example.realworldjava.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    @Query("SELECT DISTINCT t.tag FROM TagEntity t")
    List<String> findAllTags();
}
