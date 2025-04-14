package com.example.realworldjava.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleDto {

    @NotEmpty(message = "Title can't be empty")
    private String title;

    @NotEmpty(message = "Description can't be empty")
    private String description;

    @NotEmpty(message = "Body can't be empty")
    private String body;

    private List<String> tagList;
}
