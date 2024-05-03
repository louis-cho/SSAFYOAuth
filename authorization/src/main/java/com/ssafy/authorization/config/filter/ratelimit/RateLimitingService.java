package com.ssafy.authorization.config.filter.ratelimit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {

	private final RedisTemplate<String, Integer> redisIntegerTemplate;
	private final Integer MAX_REQUESTS = 100; // 시간당 IP당 최대 요청 수
	private final Integer TIME_WINDOW_IN_SECONDS = 60; // 시간 창 (초)

	public RateLimitingService(RedisTemplate<String, Integer> redisTemplate) {
		this.redisIntegerTemplate = redisTemplate;
	}

	public boolean isBlocked(String ip) {
		ValueOperations<String, Integer> ops = redisIntegerTemplate.opsForValue();
		Integer currentCount = ops.get(ip);
		if (currentCount == null) {

			ops.set(ip, 1, TIME_WINDOW_IN_SECONDS, TimeUnit.SECONDS);
			return false;
		} else {
			if (currentCount >= MAX_REQUESTS) {
				return true;
			}
			ops.increment(ip);
			return false;
		}
	}
}
