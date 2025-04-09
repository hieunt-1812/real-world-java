package com.example.realworldjava.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateArticleDto {

    private String title;

    private String description;

    private String body;
}

