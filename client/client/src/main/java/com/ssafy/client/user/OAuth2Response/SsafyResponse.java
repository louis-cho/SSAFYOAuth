package com.ssafy.client.user.OAuth2Response;

import java.util.Map;

public class SsafyResponse  implements OAuth2Response {

	private final Map<String, Object> attribute;

	public SsafyResponse(Map<String, Object> attribute) {

		this.attribute = attribute;
	}

	@Override
	public String getProvider() {

		return "ssafyOAuth";
	}

	@Override
	public String getProviderId() {

		return (String)attribute.get("email");
	}

	@Override
	public String getEmail() {

		return (String) attribute.get("email");
	}

	@Override
	public String getName() {

		return (String) attribute.get("name");
	}
}
