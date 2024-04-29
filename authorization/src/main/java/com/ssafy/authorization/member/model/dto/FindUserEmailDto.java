package com.ssafy.authorization.member.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FindUserEmailDto {
	private String userName;
	private String phoneNumber;
}
