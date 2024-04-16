package org.example.authorization.secure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptEncoder {
	private static BcryptEncoder instance = null;
	private final BCryptPasswordEncoder passwordEncoder;

	BcryptEncoder() {
		passwordEncoder = new BCryptPasswordEncoder();
	}

	public static BcryptEncoder getInstance() {
		if (instance == null) {
			instance = new BcryptEncoder();
		}
		return instance;
	}

	public String encode(String text) {
		return passwordEncoder.encode(text);
	}

	public boolean verify(String plain, String bcrypted) {
		return passwordEncoder.matches(plain, bcrypted);
	}
}
