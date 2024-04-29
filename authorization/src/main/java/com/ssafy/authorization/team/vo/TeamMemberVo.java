package com.ssafy.authorization.team.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberVo {

	private String email;
	private String name;
	private Boolean isAccept;
	private Boolean isLeader;

}
