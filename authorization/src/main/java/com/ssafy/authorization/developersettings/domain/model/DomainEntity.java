package com.ssafy.authorization.developersettings.domain.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "domain_url", indexes = {@Index(columnList = "teamId")})
public class DomainEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private UUID teamId;

	private UUID userId;

	private String domain;

	@Version
	private Integer version;

	// 생성자, 게터 및 세터, toString 등의 메서드는 필요에 따라 추가합니다.
	// 생성자와 게터/세터는 롬복(Lombok) 등의 라이브러리를 사용하여 간편하게 생성할 수 있습니다.

	// 기본 생성자
	public DomainEntity() {
	}

	// 모든 필드를 사용하는 생성자
	public DomainEntity(UUID teamId, UUID userId, String domain) {
		this.teamId = teamId;
		this.userId = userId;
		this.domain = domain;
	}

	// Getter 및 Setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domainDomain) {
		this.domain = domain;
	}

	// toString 메서드
	@Override
	public String toString() {
		return "DomainEntity{" +
			"id=" + id +
			", teamId=" + teamId +
			", userId=" + userId +
			", domain='" + domain + '\'' +
			'}';
	}
}
