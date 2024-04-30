package com.ssafy.authorization.link.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

public interface LinkService {
	Map listLink(Authentication authentication);

	Map removeLink(Integer teamSeq, Authentication authentication);

	Map listCustomer(Integer teamSeq, Authentication authentication);

	Map searchCustomer(Integer teamSeq, String keyword, Authentication authentication);

	Map removeCustomer(Integer teamSeq, Integer memberSeq, Authentication authentication);
}
