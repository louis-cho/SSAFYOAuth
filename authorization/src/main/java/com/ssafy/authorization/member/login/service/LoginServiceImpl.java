package com.ssafy.authorization.member.login.service;

import com.ssafy.authorization.member.login.management.LoginQueueManager;
import com.ssafy.authorization.member.login.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginQueueManager loginQueueManager;

    @Autowired
    public LoginServiceImpl(LoginQueueManager loginQueueManager) {
        this.loginQueueManager = loginQueueManager;
    }

    @Override
    public boolean addLoginRequest(LoginRequest loginRequest) {
        return loginQueueManager.enqueue(loginRequest);
    }

    @Override
    public LoginQueueManager getLoginQueueManager() {
        return loginQueueManager;
    }
}
