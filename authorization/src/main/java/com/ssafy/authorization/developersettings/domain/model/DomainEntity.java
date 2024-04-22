package com.ssafy.authorization.developersettings.domain.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "domain_url", indexes = {@Index(columnList = "teamId")}, uniqueConstraints = {
	@UniqueConstraint(columnNames = {"domain", "teamId", "userId"})
})
@Getter
@Setter
@IdClass(DomainEntityId.class)
public class DomainEntity implements Serializable {
	@Id
	private UUID teamId;
	@Id
	private UUID userId;
	@Id
	private String domain;

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

	@Override
	public String toString() {
		return "DomainEntity{" +
			" teamId=" + teamId +
			", userId=" + userId +
			", domain='" + domain + '\'' +
			'}';
	}
}
