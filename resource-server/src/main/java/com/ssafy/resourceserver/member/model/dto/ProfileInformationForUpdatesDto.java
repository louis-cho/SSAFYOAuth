package com.ssafy.resourceserver.member.model.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class ProfileInformationForUpdatesDto {
	private String profileUrl;

	private Boolean gender;
	private String phoneNumber;
	private MultipartFile image;

	private String studentId;

	private String name;
	private String email;

}
