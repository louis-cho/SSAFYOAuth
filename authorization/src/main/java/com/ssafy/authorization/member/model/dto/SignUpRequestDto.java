package com.ssafy.authorization.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequestDto {
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 입력이 올바르지 않습니다.")
	private String userEmail;

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
		message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
	private String password;

	@NotEmpty(message = "유저명은 필수입니다.")
	private String userName;

	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰의 양식과 맞지 않습니다. xxx-xxxx-xxxx")
	private String phoneNumber;

	private String gender;

	@Pattern(regexp = "^[0-9]+$", message = "학번은 숫자만 입력 가능합니다.")
	private String studentId;

	private MultipartFile profileImage;  // 프로필 이미지 파일

	@Override
	public String toString() {
		return "SignUpRequestDto{" +
			"userEmail='" + userEmail + '\'' +
			", password='" + password + '\'' +
			", userName='" + userName + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", gender=" + gender +
			", studentId='" + studentId + '\'' +
			", profileImageFileName=" + (profileImage != null ? profileImage.getOriginalFilename() : "None") +
			'}';
	}
}
