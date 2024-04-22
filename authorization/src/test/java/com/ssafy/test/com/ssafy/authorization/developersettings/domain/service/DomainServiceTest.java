package com.ssafy.test.com.ssafy.authorization.developersettings.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.ssafy.authorization.AuthorizationApplication;
import com.ssafy.authorization.developersettings.domain.model.DomainEntity;
import com.ssafy.authorization.developersettings.domain.service.DomainService;

@SpringBootTest(classes = AuthorizationApplication.class)
public class DomainServiceTest {

	@Autowired
	private DomainService domainService;

	@Test
	@DirtiesContext
	public void testConcurrentCommit() throws InterruptedException {
		// 동시에 여러 개의 도메인을 저장하는 테스트
		UUID teamId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		String domainUrl = "example.com";

		for (int i = 0; i < 7; i++) {
			domainService.insertDomain(new DomainEntity(teamId, userId, domainUrl + i));
		}

		// 저장된 도메인 개수 확인
		int count = domainService.countDomainUrl(teamId);
		// 최대 6개까지 저장되어야 함
		assertTrue(count <= 6);

		for (int i = 0; i < 7; i++) {
			domainService.removeDomain(new DomainEntity(teamId, userId, domainUrl + i));
		}

		count = domainService.countDomainUrl(teamId);

		assertEquals(0, count);
	}
}
