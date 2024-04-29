package com.ssafy.authorization.link.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

public interface LinkService {
	Map listLink(Authentication authentication);
}
