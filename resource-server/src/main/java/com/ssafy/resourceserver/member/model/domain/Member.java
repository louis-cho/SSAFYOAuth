package com.ssafy.resourceserver.member.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "authorities" })
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



}

