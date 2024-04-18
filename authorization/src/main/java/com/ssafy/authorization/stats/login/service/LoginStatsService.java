package com.ssafy.authorization.stats.login.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.stats.login.model.LoginDocument;

@Service
public interface LoginStatsService {
	List<LoginDocument> findByUserId(String userId, String teamId);

	List<LoginDocument> findByTeamId(String teamId);

	int saveUserLogin(LoginDocument loginDocument);
}
