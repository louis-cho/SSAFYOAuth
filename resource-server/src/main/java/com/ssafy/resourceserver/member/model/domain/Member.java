package com.ssafy.resourceserver.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.resourceserver.member.model.dto.ProfileInformationForUpdatesDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "authorities" })
@ToString
public class Member{
	@Id
	@Column(name = "member_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;

	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private MemberRoleEnum role;

	@Column
	private String phoneNumber;

	@Column
	private String studentId;

	@Column
	private Integer grade;

	@Column
	private String track;

	@Column
	private String name;

	@Column
	private LocalDateTime deleteDate;

	@Column
	private Boolean isDelete;

	@Column
	private String image;

	@Column
	private Boolean gender;

	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private Boolean enabled;

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public void changeProfile(String imageUrl) {
		this.image = imageUrl;
	}

	public void changePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changeGrade(Integer grade) {
		this.grade = grade;
	}


	public ProfileInformationForUpdatesDto EntityToProfileUpdatesDto(){
		return ProfileInformationForUpdatesDto.builder()
			.gender(gender)
			.profileUrl(image)
			.studentId(studentId)
			.name(name)
			.phoneNumber(phoneNumber)
			.email(email)
			.build();
	}
}

