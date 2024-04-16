package org.example.authorization.secure;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KeyManagerCleanUpTask {

	private final int period = 600000;

	@Scheduled(fixedRate = period) // 10 min
	public void cleanupUnusedKeys() {
		for (String ip : KeyManager.getInstnace().getKeyMap().keySet()) {
			// Implement logic to check if there were no requests for the last 10 minutes
			if (noRequest(ip)) {
				System.out.println("ip >> " + ip + " 지움");
				KeyManager.getInstnace().getKeyMap().remove(ip);
			}
		}
	}

	private boolean noRequest(String ip) {
		long lastRequestTime = KeyManager.getInstnace().getLastRequest().getOrDefault(ip, 0L);
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - lastRequestTime;

		return elapsedTime >= period; // 10 분
	}
}