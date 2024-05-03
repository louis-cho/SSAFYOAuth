package com.ssafy.resourceserver.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String username;
    private String sub;
    private String scope;
    private String email;


}