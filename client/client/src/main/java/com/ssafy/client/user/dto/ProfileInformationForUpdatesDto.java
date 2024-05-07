package com.ssafy.client.user.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class ProfileInformationForUpdatesDto {
	private String profileUrl;
	private MultipartFile image;
	private Boolean gender;
	private String phoneNumber;

	private String studentId;

	private String name;
	private String email;

}
