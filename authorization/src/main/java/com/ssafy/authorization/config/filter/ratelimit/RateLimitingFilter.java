package com.ssafy.authorization.config.filter.ratelimit;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

	private final RateLimitingService rateLimitingService;

	public RateLimitingFilter(RateLimitingService rateLimitingService) {
		log.debug("RateLimitingFilter 실행됨");
		this.rateLimitingService = rateLimitingService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return new AntPathRequestMatcher("/css/**").matches(request); // 제외할 경로
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String ip = request.getRemoteAddr();
		log.debug("ip는 이렇다구 {}", ip);
		if (rateLimitingService.isBlocked(ip)) {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.getWriter().write("요청이 너무 많습니다");
			return;
		}
		filterChain.doFilter(request, response);
	}
}
