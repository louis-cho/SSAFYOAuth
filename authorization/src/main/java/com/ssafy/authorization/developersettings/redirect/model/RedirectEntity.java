package com.ssafy.authorization.developersettings.redirect.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "redirect_url", indexes = {@Index(columnList = "teamId")}, uniqueConstraints = {
	@UniqueConstraint(columnNames = {"redirect", "teamId", "userId"})
})
@Getter
@Setter
@IdClass(RedirectEntityId.class)
public class RedirectEntity implements Serializable {
	@Id
	private int teamId;
	@Id
	private int userId;
	@Id
	private String redirect;

	// 생성자, 게터 및 세터, toString 등의 메서드는 필요에 따라 추가합니다.
	// 생성자와 게터/세터는 롬복(Lombok) 등의 라이브러리를 사용하여 간편하게 생성할 수 있습니다.

	// 기본 생성자
	public RedirectEntity() {
	}

	// 모든 필드를 사용하는 생성자
	public RedirectEntity(int teamId, int userId, String redirect) {
		this.teamId = teamId;
		this.userId = userId;
		this.redirect = redirect;
	}

	@Override
	public String toString() {
		return "RedirectEntity{" +
			" teamId=" + teamId +
			", userId=" + userId +
			", redirect='" + redirect + '\'' +
			'}';
	}
}