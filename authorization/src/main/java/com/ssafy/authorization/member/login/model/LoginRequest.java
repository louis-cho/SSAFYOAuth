package com.ssafy.authorization.member.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {
    private String username;
    private Integer teamId;
    private String ip;


    public LoginRequest(String username, Integer teamId) {
        this.username = username;
        this.teamId = teamId;
    }

}