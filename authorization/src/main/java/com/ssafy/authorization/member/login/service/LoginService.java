package com.ssafy.authorization.member.login.service;

import com.ssafy.authorization.member.login.management.LoginQueueManager;
import com.ssafy.authorization.member.login.model.LoginRequest;

public interface LoginService {
    boolean addLoginRequest(LoginRequest loginRequest);

    LoginQueueManager getLoginQueueManager();

}
