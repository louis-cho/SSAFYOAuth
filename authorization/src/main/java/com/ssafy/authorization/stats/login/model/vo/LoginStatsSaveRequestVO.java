package com.ssafy.authorization.stats.login.model.vo;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginStatsSaveRequestVO {

	public UUID userId;
	public UUID teamId;
	public Instant createdAt;
	public boolean success;
}
