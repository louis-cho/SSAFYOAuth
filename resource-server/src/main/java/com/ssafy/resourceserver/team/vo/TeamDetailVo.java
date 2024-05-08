package com.ssafy.resourceserver.team.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDetailVo {
	private Integer teamSeq;
	private String teamName;
	private String serviceName;
	private String serviceKey;
	private List<String> domainUrl;
	private List<String> redirectUrl;
	private List<TeamMemberVo> members;
	private String image;
}
