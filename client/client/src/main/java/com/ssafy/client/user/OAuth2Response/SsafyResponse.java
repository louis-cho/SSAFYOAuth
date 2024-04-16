package com.ssafy.client.user.OAuth2Response;

import java.util.Map;

public class SsafyResponse  implements OAuth2Response {

	private final Map<String, Object> attribute;

	public SsafyResponse(Map<String, Object> attribute) {

		this.attribute = attribute;
	}

	@Override
	public String getProvider() {

		return "oidc-client";
	}

	@Override
	public String getProviderId() {

		return attribute.get("sub").toString();
	}

	@Override
	public String getEmail() {

		return attribute.get("aud").toString();
	}

	@Override
	public String getName() {

		return attribute.get("aud").toString();
	}
}
