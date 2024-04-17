package com.ssafy.test.com.ssafy.authorization.developersettings.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
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

		// 여러 개의 도메인을 저장할 리스트 생성
		List<DomainEntity> domains = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			domains.add(new DomainEntity(teamId, userId, domainUrl + i));
		}

		// 도메인 저장을 병렬로 실행하는 스레드 리스트 생성
		List<Thread> threads = new ArrayList<>();
		for (DomainEntity domain : domains) {
			threads.add(new Thread(() -> {
				// 각 스레드는 하나의 도메인을 저장하도록 함
				int result = domainService.saveDomain(domain);
				// 저장에 성공하면 1이 반환되어야 함
				assertEquals(1, result);
			}));
		}

		// 생성된 스레드 시작
		for (Thread thread : threads) {
			thread.start();
		}

		// 모든 스레드가 종료될 때까지 대기
		for (Thread thread : threads) {
			thread.join();
		}

		// 저장된 도메인 개수 확인
		int count = domainService.countDomainUrl(teamId);
		// 최대 6개까지 저장되어야 함
		assertTrue(count <= 6);
	}
}
