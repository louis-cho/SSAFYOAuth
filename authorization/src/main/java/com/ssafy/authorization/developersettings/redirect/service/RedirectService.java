package com.ssafy.authorization.developersettings.redirect.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ssafy.authorization.developersettings.redirect.model.RedirectEntity;

@Service
public interface RedirectService {

	int insertRedirect(RedirectEntity redirectEntity);

	int removeRedirect(RedirectEntity redirectEntity);

	int countRedirectUrl(UUID teamId);
}
