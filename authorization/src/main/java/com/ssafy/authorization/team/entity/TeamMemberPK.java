package com.ssafy.authorization.team.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberPK implements Serializable {

	private Integer teamSeq;

	private Integer memberSeq;

}
