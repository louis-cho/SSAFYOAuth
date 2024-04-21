package com.ssafy.authorization.stats.login.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.stats.login.model.LoginStats;
import com.ssafy.authorization.stats.login.model.VO.LoginStatsFetchRequestVO;

@Service
public interface LoginStatsService {

	// 생성
	public void save(LoginStats loginStats);

	// 삭제
	public void delete(String userId, String teamId);

	// 조회
	public List<LoginStats> fetch(String userId, String teamId);

	public List<LoginStats> fetch2(LoginStatsFetchRequestVO requestVO);

}
