package com.ssafy.client.user.service;

import com.ssafy.client.user.domain.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        log.info("{}, {} ,{} authenticaton", authentication.getName(), authentication.getAuthorities(), authentication.getAuthorizedClientRegistrationId());
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        log.info("{} re : {} usrName", registrationId, user.getUsername());

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                registrationId, user.getUsername());

        return client.getAccessToken().getTokenValue();
    }

    public String getRefreshToken(OAuth2AuthenticationToken authentication) {
        log.info("{}, {} ,{} getRefreshToken", authentication.getName(), authentication.getAuthorities(), authentication.getAuthorizedClientRegistrationId());
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        log.info("{} re : {} usrName", registrationId, user.getUsername());

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                registrationId, user.getUsername());

        return client.getRefreshToken().getTokenValue();
    }

}
