package com.ssafy.resourceserver.redirect.service;

import org.springframework.stereotype.Service;

import com.ssafy.resourceserver.redirect.model.RedirectEntity;

@Service
public interface RedirectService {

	int insertRedirect(RedirectEntity redirectEntity);

	int removeRedirect(RedirectEntity redirectEntity);

	int countRedirectUrl(int teamId);
}
