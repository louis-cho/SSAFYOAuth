package com.ssafy.authorization.redirect.service;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.redirect.model.RedirectEntity;

@Service
public interface RedirectService {

	int insertRedirect(RedirectEntity redirectEntity);

	int removeRedirect(RedirectEntity redirectEntity);

	int countRedirectUrl(int teamId);
}
