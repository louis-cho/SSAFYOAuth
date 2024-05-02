package com.ssafy.resourceserver.team.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberWithInfoPK implements Serializable {
	private Integer teamSeq;
	private Integer memberSeq;
}
