package com.ssafy.resourceserver.team.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamListVo {
	private String teamName;
	private String serviceName;
	private Boolean isLeader;
	private Integer teamSeq;
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	private Boolean isAccept;
}
