package com.ssafy.resourceserver.team.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "developer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperEntity {
	@Id
	@Column
	private Integer memberSeq;

	@Column
	private LocalDateTime createDate;

	@Column
	@Nullable
	private LocalDateTime deleteDate;

	@Column
	private Boolean isDelete;

	public DeveloperEntity(Integer memberSeq, Boolean isDelete) {
		this.memberSeq=memberSeq;
		this.createDate = LocalDateTime.now();
		this.isDelete=isDelete;
	}
}
