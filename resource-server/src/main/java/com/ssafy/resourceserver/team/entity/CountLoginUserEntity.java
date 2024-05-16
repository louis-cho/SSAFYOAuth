package com.ssafy.resourceserver.team.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "count_login_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountLoginUserEntity {
	@Id
	@Column
	private String serviceId;

	@Column
	private Long loginCount;
}
