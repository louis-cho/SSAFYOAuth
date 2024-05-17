package com.ssafy.authorization.member.login.filter;

import com.ssafy.authorization.member.login.model.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;

public class LoginRateLimiter {

    static public String getCountIndex(LoginRequest request) {
        String ip = request.getIp();
        String teamId = String.valueOf(request.getTeamId());

        return ip + "_" + teamId;
    }

    static public String getIndex(LoginRequest request) {
        String ip = request.getIp();
        String teamId = String.valueOf(request.getTeamId());

        return ip + ":" + teamId;
    }



}
