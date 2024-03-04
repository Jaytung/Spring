package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String account;
    private String name;

    public LoginResponse(String account, String name) {
        this.account = account;
        this.name = name;
    }
}
