package org.example.authorization.member.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequestDto {
	@NotEmpty
	private String userEmail;
	@NotEmpty
	private String password;
	private String userName;
	private String gender;
	private String phoneNumber;

	@Override
	public String toString() {
		return "SignUpRequestDto{" +
			"userEmail='" + userEmail + '\'' +
			", password='" + password + '\'' +
			", userName='" + userName + '\'' +
			", gender='" + gender + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			'}';
	}
}
