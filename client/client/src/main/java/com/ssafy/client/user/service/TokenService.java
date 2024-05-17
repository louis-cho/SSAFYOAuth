package com.ssafy.client.user.service;

import com.ssafy.client.user.domain.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

@Component
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
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        String principalName = authentication.getName();

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(registrationId, principalName);

        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
            return authorizedClient.getRefreshToken().getTokenValue();
        }

        return null;
    }

    public OAuth2AccessToken getAccessTokenObject(OAuth2AuthenticationToken authentication) {
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        String principalName = authentication.getName();

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(registrationId, principalName);
        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
            return authorizedClient.getAccessToken();
        }
        return null;
    }
    public OAuth2RefreshToken getRefreshTokenObject(OAuth2AuthenticationToken authentication) {
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        String principalName = authentication.getName();

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(registrationId, principalName);
        if (authorizedClient != null && authorizedClient.getRefreshToken() != null) {
            return authorizedClient.getRefreshToken();
        }
        return null;
    }
    public void saveTokenData(OAuth2AuthorizedClient authorizedClient,OAuth2AuthenticationToken authentication){
        authorizedClientService.saveAuthorizedClient(authorizedClient, authentication);

    }

//    public String getRefreshToken(OAuth2AuthenticationToken authentication) {
//        log.info("{}, {} ,{} getRefreshToken", authentication.getName(), authentication.getAuthorities(), authentication.getAuthorizedClientRegistrationId());
//        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
//        String registrationId = authentication.getAuthorizedClientRegistrationId();
//        log.info("{} re : {} usrName", registrationId, user.getUsername());
//
//        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
//                registrationId, user.getUsername());
//
//        return client.getRefreshToken().getTokenValue();
//    }

}
