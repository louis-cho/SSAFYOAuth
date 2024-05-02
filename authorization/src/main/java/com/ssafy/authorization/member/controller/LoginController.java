package com.ssafy.authorization.member.controller;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginController {

	private final OAuth2AuthorizationConsentService authorizationConsentService;

	public LoginController(OAuth2AuthorizationConsentService authorizationConsentService) {
		this.authorizationConsentService = authorizationConsentService;
	}

	@GetMapping(value = "/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/login_test")
	public String login_test() {
		return "login_test";
	}

	@GetMapping(value = "/oauth2/consent")
	public String consent(
			Principal principal,
			Model model,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.STATE) String state
	) {

		Set<String> scopesToApprove = new HashSet<>();
		Set<String> previouslyApprovedScopes = new HashSet<>();
		OAuth2AuthorizationConsent previousConsent = this.authorizationConsentService.findById(clientId, principal.getName());
		for (String scopeFromRequest : StringUtils.delimitedListToStringArray(scope, " ")) {
			if (previousConsent != null && previousConsent.getScopes().contains(scopeFromRequest)) {
				previouslyApprovedScopes.add(scopeFromRequest);
			} else {
				scopesToApprove.add(scopeFromRequest);
			}
		}

		model.addAttribute("state", state);
		model.addAttribute("clientId", clientId);
		model.addAttribute("scopes", withDescription(scopesToApprove));
		model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
		model.addAttribute("principalName", principal.getName());

		return "consent";
	}

	private Set<ScopeWithDescription> withDescription(Set<String> scopes) {
		return scopes
				.stream()
				.map(ScopeWithDescription::new)
				.collect(Collectors.toSet());
	}

	static class ScopeWithDescription {

		public final String scope;
		public final String description;

		private final static String DEFAULT_DESCRIPTION = "알 수 없는 권한 - 이 권한에 대한 정보를 제공할 수 없으므로 권한을 부여할 때 주의하시기 바랍니다.";
		private static final Map<String, String> scopeDescriptions = new HashMap<>();
		static {
			scopeDescriptions.put(
					"openid",
					"오픈 아이디를 이용해 사용자를 합니다."
			);
			scopeDescriptions.put(
					"profile",
					"프로필 정보를 가져 옵니다."
			);
			scopeDescriptions.put(
				"email",
				"이메일 정보 제공을 동의합니다."
			);
			scopeDescriptions.put(
				"image",
				"프로필 사진 정보 제공을 동의합니다."
			);
			scopeDescriptions.put(
				"name",
				"프로필 이름 제공을 동의합니다."
			);
			scopeDescriptions.put(
				"gender",
				"프로필 성별 제공을 동의합니다."
			);
			scopeDescriptions.put(
				"phoneNumber",
				"프로필 전화번호 제공을 동의합니다."
			);scopeDescriptions.put(
				"studentId",
				"프로필 학번 정보 제공을 동의합니다."
			);
		}

		ScopeWithDescription(String scope) {
			this.scope = scope;
			this.description = scopeDescriptions.getOrDefault(scope, DEFAULT_DESCRIPTION);
		}
	}
}