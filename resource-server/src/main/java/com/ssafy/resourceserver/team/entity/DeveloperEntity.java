package com.ssafy.resourceserver.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="developer")
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperEntity {

	@Id
	public Integer memberSeq;
	public boolean isDelete;

}
