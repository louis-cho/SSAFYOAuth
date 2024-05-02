package com.ssafy.resourceserver.team.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteListVo {
	private Integer teamSeq;
	private String teamName;
	private String leader;
}
