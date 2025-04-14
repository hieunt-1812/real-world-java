package com.example.realworldjava.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private String email;

    private String username;

    private String image;

    private String bio;

    private Boolean demo;
}