package com.example.dto;

import lombok.Builder;

public class UserInfo {

    private String email;
    private String name;
    private String group;

    @Builder
    public UserInfo(String email, String name, String group) {
        this.email = email;
        this.name = name;
        this.group = group;
    }
}
