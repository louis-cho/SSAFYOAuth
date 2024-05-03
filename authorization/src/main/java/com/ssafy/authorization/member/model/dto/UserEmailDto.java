package com.ssafy.authorization.member.model.dto;

import lombok.Data;

@Data
public class UserEmailDto {
	private String userEmail;

	// Getters and Setters
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
