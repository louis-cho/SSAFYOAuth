package com.ssafy.resourceserver.redirect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.resourceserver.redirect.model.RedirectEntity;
import com.ssafy.resourceserver.redirect.repository.RedirectEntityRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class RedirectServiceImpl implements RedirectService {

	private final RedirectEntityRepository redirectEntityRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	RedirectServiceImpl(RedirectEntityRepository redirectEntityRepository) {
		this.redirectEntityRepository = redirectEntityRepository;
	}

	@Override
	@Transactional
	public int insertRedirect(RedirectEntity redirect) {
		int teamId = redirect.getTeamId();
		int userId = redirect.getUserId();
		String redirectUrl = redirect.getRedirect();

		if (!isTeamMember(teamId, userId)) {
			// 팀원이 아닌 경우
			return -3;
		}

		try {
			int count = countRedirectUrl(teamId);
			// 도메인 URL 개수가 6개 미만인 경우에만 URL 추가
			if (count < 6) {
				RedirectEntity redirectEntity = new RedirectEntity(teamId, 0, redirectUrl);
				redirectEntityRepository.save(redirectEntity);
			}
		} catch (Exception e) {
			return -1;
		}

		return 1;
	}

	@Override
	@Transactional
	public int removeRedirect(RedirectEntity redirect) {
		int teamId = redirect.getTeamId();
		int userId = redirect.getUserId();
		String redirectUrl = redirect.getRedirect();

		if (!isTeamMember(teamId, userId)) {
			// 팀원이 아닌 경우
			return -3;
		}

		try {
			RedirectEntity redirectEntity = new RedirectEntity(teamId, 0, redirectUrl);
			redirectEntityRepository.delete(redirectEntity);
		} catch (Exception e) {
			return -1;
		}
		return 1;
	}

	private boolean isTeamMember(int teamId, int userId) {
		// ToDO: 팀원 판별 기능 호출하기
		return true; // 임시로 true 반환
	}

	@Override
	@Transactional(readOnly = true)
	public int countRedirectUrl(int teamId) {
		return redirectEntityRepository.countByTeamId(teamId);
	}
}
