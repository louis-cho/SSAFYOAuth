package com.ssafy.resourceserver.member.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.resourceserver.common.domain.BaseTimeEntity;
import com.ssafy.resourceserver.member.model.dto.SignUpRequestDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "authorities" })
public class Member extends BaseTimeEntity implements UserDetails {
	@Id
	@Column(name = "member_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(unique = true)
	@JsonProperty("username")
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
	private Boolean isDeleted;

	@Column
	private String image;

	@Column
	private Short gender;




	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private Boolean enabled;

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	@JsonProperty("username")  // 이제 getUsername() 호출 시 JSON에서는 "username"으로 표현
	public String getEmail() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		MemberRoleEnum role = this.getRole();
		String authority = role.getAuthority();

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_"+authority);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(simpleGrantedAuthority);

		return authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	public void changePassword(String newPassword) {
		this.password = newPassword;
	}

	public void changeProfile(String imageUrl) {
		this.image = imageUrl;
	}
	public static Member create(SignUpRequestDto dto) {
		return Member.builder()
			.email(dto.getUserEmail())
			.name(dto.getUserName())
			.grade(1)
			.password(dto.getPassword())
			.gender(Short.parseShort(dto.getGender()))
			.phoneNumber(dto.getPhoneNumber())
			.studentId(dto.getStudentId())
			.accountNonExpired(true)
			.accountNonLocked(true)
			.credentialsNonExpired(true)
			.enabled(true)
			.role(MemberRoleEnum.USER)
			.isDeleted(false)
			.build();
	}
}

