package com.ssafy.authorization.utils;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final LoginUrlAuthenticationEntryPoint delegate;

	public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		this.delegate = new LoginUrlAuthenticationEntryPoint(loginFormUrl);
	}


	protected String buildRedirectUrlToLoginPage(HttpServletRequest request) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(delegate.getLoginFormUrl());
		request.getParameterMap().forEach((key, values) -> {
			for (String value : values) {
				uriBuilder.queryParam(key, value);
			}
		});
		return uriBuilder.toUriString();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		String redirectUrl = buildRedirectUrlToLoginPage(request);
		response.sendRedirect(redirectUrl);
	}
}
