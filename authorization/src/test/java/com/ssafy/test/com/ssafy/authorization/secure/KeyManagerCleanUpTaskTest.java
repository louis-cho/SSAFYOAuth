package com.ssafy.test.com.ssafy.authorization.secure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ssafy.authorization.secure.KeyManager;
import com.ssafy.authorization.secure.KeyManagerCleanUpTask;
import com.ssafy.authorization.secure.RSA2048;

public class KeyManagerCleanUpTaskTest {

	// 테스트용 KeyManagerCleanUpTask 객체 생성
	private KeyManagerCleanUpTask task = new KeyManagerCleanUpTask();

	@Test
	public void testCleanupUnusedKeys() {

		KeyManager.getInstance().getKeyMap().put("192.168.0.1", RSA2048.createKey());
		// 5분보다 더 이전에 요청이 있었던 IP 주소 추가
		KeyManager.getInstance().getLastRequest().put("192.168.0.1", System.currentTimeMillis() - task.period - 100);

		// cleanupUnusedKeys() 메소드 호출
		task.cleanupUnusedKeys();

		// IP 주소가 KeyMap에서 제거되었는지 확인
		assertFalse(KeyManager.getInstance().getKeyMap().containsKey("192.168.0.1"));

		// 현재 시간을 5분 이전으로 설정하여 요청이 있었던 IP 주소 추가
		KeyManager.getInstance().getKeyMap().put("192.168.0.2", RSA2048.createKey());
		KeyManager.getInstance().getLastRequest().put("192.168.0.2", System.currentTimeMillis());

		// cleanupUnusedKeys() 메소드 호출
		task.cleanupUnusedKeys();

		// IP 주소가 KeyMap에 남아 있는지 확인
		assertTrue(KeyManager.getInstance().getKeyMap().containsKey("192.168.0.2"));
	}
}
