// package com.ssafy.test.com.ssafy.authorization.developersettings.redirect.service;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.util.UUID;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.annotation.DirtiesContext;
//
// import com.ssafy.authorization.AuthorizationApplication;
// import com.ssafy.authorization.redirect.model.RedirectEntity;
// import com.ssafy.authorization.redirect.service.RedirectService;
//
// @SpringBootTest(classes = AuthorizationApplication.class)
// public class RedirectServiceTest {
//
// 	@Autowired
// 	private RedirectService redirectService;
//
// 	@Test
// 	@DirtiesContext
// 	public void testConcurrentCommit() throws InterruptedException {
// 		// 동시에 여러 개의 도메인을 저장하는 테스트
// 		UUID teamId = UUID.randomUUID();
// 		UUID userId = UUID.randomUUID();
// 		String redirectUrl = "example.com";
//
// 		for (int i = 0; i < 7; i++) {
// 			redirectService.insertRedirect(new RedirectEntity(teamId, userId, redirectUrl + i));
// 		}
//
// 		// 저장된 도메인 개수 확인
// 		int count = redirectService.countRedirectUrl(teamId);
// 		// 최대 6개까지 저장되어야 함
// 		assertTrue(count <= 6);
//
// 		for (int i = 0; i < 7; i++) {
// 			redirectService.removeRedirect(new RedirectEntity(teamId, userId, redirectUrl + i));
// 		}
//
// 		count = redirectService.countRedirectUrl(teamId);
//
// 		assertEquals(0, count);
// 	}
// }
