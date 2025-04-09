package com.example.realworldjava.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@JsonTypeName("profile")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@Data
@Builder
@AllArgsConstructor
public class Profile {

    private String username;

    private String image;

    private String bio;

    @Builder.Default
    private Boolean following = false;
}
