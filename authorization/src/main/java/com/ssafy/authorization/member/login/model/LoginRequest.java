package com.ssafy.authorization.member.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    private String username;
    private String password;
    private int teamId;
    private int userId;

    public LoginRequest(String username, String password, int teamId, int userId) {
        this.username = username;
        this.password = password;
        this.teamId = teamId;
        this.userId = userId;
    }

    // getter 및 setter 메서드
}