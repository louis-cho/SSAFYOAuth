package com.ssafy.authorization.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamAddDto {
	private String teamName;
	private String serviceName;
	private String[] members;
	private String[] domainUrl;
	private String[] redirectUrl;
	private Integer leaderMemberSeq;
}
