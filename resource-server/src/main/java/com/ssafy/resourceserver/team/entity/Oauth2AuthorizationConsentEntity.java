package com.ssafy.resourceserver.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "oauth2_authorization_consent")
@Data
public class Oauth2AuthorizationConsentEntity {
	@Id
	@Column
	private String registeredClientId;

	@Column
	private String principalName;

	@Column
	private String authorities;
}
