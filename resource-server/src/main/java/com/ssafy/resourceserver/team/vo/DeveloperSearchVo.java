package com.ssafy.resourceserver.team.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperSearchVo {
	private String email;
	private String name;
	private String image;
}
