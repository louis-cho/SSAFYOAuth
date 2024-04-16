package com.ssafy.authorization.developersettings.domain.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DomainEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private UUID teamId;

	private UUID userId;

	private String domainUrl;

	// 생성자, 게터 및 세터, toString 등의 메서드는 필요에 따라 추가합니다.
	// 생성자와 게터/세터는 롬복(Lombok) 등의 라이브러리를 사용하여 간편하게 생성할 수 있습니다.

	// 기본 생성자
	public DomainEntity() {
	}

	// 모든 필드를 사용하는 생성자
	public DomainEntity(UUID teamId, UUID userId, String domainUrl) {
		this.teamId = teamId;
		this.userId = userId;
		this.domainUrl = domainUrl;
	}

	// Getter 및 Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getTeamId() {
		return teamId;
	}

	public void setTeamId(UUID teamId) {
		this.teamId = teamId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	// toString 메서드
	@Override
	public String toString() {
		return "DomainEntity{" +
			"id=" + id +
			", teamId=" + teamId +
			", userId=" + userId +
			", domainUrl='" + domainUrl + '\'' +
			'}';
	}
}
