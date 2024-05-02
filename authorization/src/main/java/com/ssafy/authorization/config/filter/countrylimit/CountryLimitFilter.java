package com.ssafy.authorization.config.filter.countrylimit;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryLimitFilter extends OncePerRequestFilter {
	private final CountryResolver countryResolver;
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return new AntPathRequestMatcher("/css/**").matches(request); // 제외할 경로
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletRequest req = (HttpServletRequest) request;
		String ip = req.getRemoteAddr();
		System.out.println("Client IP Address: " + ip);

		String country = countryResolver.resolveCountry(ip);
		log.info("ip는 {}, 나라는 {}", ip, country);
		// 일단 국가 테스트
		if ("KR".equals(country)) {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
	}
		filterChain.doFilter(request, response);
	}

	// @Override
	// public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	// 	throws IOException, ServletException {
	// 	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	// 	String ip = httpServletRequest.getRemoteAddr();
	// 	String country = countryResolver.resolveCountry(ip);
	// 	log.debug("ip는 {}, 나라는 {}", ip, country);
	// 	// 일단 국가 테스트
	// 	// if ("KR".equals(country)) {
	// 	// 	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	// 	// 	httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	// 	// 	return;
	// 	// }
	// 	// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// 	// if (authentication != null && authentication.isAuthenticated()) {
	// 	// 	String username = authentication.getName();
	// 	// 	Set<String> allowedCountries = userService.getAllowedCountries(username);
	// 	// 	if (!allowedCountries.contains(country)) {
	// 	// 		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	// 	// 		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	// 	// 		return;
	// 	// 	}
	// 	// }
	//
	// 	chain.doFilter(request, response);
	// }
}
